package dev.ronnieotieno.imageloaderlibrary

import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.ImageView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.net.URL


class ImageLoader : ComponentCallbacks2 {

    private lateinit var imageUrl: String
    private lateinit var imageView: ImageView


    lateinit var memoryCache: LruCache<String, Bitmap>
    private lateinit var job: Job


    fun loadImage(imageUrl: String, imageView: ImageView) {

        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {

                return bitmap.byteCount / 1024
            }
        }

        job = CoroutineScope(IO).launch {

            loadBitmap(imageUrl, imageView)

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