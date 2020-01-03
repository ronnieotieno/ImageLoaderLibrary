package dev.ronnieotieno.imageloaderlibrary

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.ImageView
import android.widget.Toast
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.net.URL


class ImageLoader(private val context: Context) : ComponentCallbacks2 {

    lateinit var memoryCache: LruCache<String, Bitmap>
    private lateinit var job: Job


    fun load(imageUrl: String, imageView: ImageView) {

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

                GlobalScope.launch(Main) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }

        CoroutineScope(IO + job).launch {

            loadBitmap(imageUrl, imageView)

        }

    }

    fun cancel() {
        if (::job.isInitialized) {
            if (job.isActive) {
                job.cancel(CancellationException("Resetting job"))
            }
        }
    }

    suspend fun loadBitmap(imageUrl: String, imageView: ImageView) {

        val bitmap: Bitmap? = memoryCache[imageUrl]

        if (bitmap != null) {
            withContext(Dispatchers.Main) {
                imageView.setImageBitmap(bitmap)
            }
        } else {
            val url = URL(imageUrl)
            val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())

            withContext(Dispatchers.Main) {
                imageView.setImageBitmap(image)
            }
            memoryCache.put(imageUrl, image)
        }
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