package dev.ronnieotieno.imageloaderlibrary

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.LruCache
import android.widget.ImageView
import android.widget.Toast
import kotlinx.coroutines.*
import java.net.URL


class ImageLoader(private val context: Context) : ComponentCallbacks2 {

    private lateinit var memoryCache: LruCache<String, Bitmap>
    private lateinit var job: Job


    fun load(dataSource: Any? = null, destination: Any? = null) {

        if (dataSource != null && dataSource is String && destination != null && destination is ImageView) {
            getImage(dataSource, destination)
        }


    }

    private fun getImage(imageUrl: String, imageView: ImageView) {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {

                return bitmap.byteCount / 1024
            }
        }

        job = Job()
        job.invokeOnCompletion {
            it?.message.let {
                var msg = it
                if (msg.isNullOrBlank()) {
                    msg = "Unknown cancellation error."
                }

                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }

        CoroutineScope(Dispatchers.IO + job).launch {

            loadBitmap(imageUrl, imageView)

        }
    }

    fun cancel() {
        if (::job.isInitialized) {
            if (job.isActive) {
                job.cancel(CancellationException("Cancelled"))
            }
        }
    }

    private suspend fun loadBitmap(imageUrl: String, imageView: ImageView) {

        val bitmap: Bitmap? = memoryCache[imageUrl]

        if (bitmap != null) {
            withContext(Dispatchers.Main) {
                imageView.setImageBitmap(bitmap)
            }
        } else {
            val url = URL(imageUrl)


            if (isInternetAvailable(context)) {
                try {
                    val image =
                        BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    withContext(Dispatchers.Main) {
                        imageView.setImageBitmap(image)
                    }
                    if (imageUrl != null && image != null) {
                        //image?.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                        memoryCache.put(imageUrl, image)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    override fun onLowMemory() {

    }

    override fun onConfigurationChanged(p0: Configuration) {

    }

    override fun onTrimMemory(level: Int) {
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            memoryCache.evictAll()
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            memoryCache.trimToSize(memoryCache.size() / 2)
        }
    }
}