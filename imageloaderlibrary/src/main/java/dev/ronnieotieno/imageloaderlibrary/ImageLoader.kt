package dev.ronnieotieno.imageloaderlibrary

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class ImageLoader {

    val memcache = MemoryCache()

    fun load(imageUrl: String, imageView: ImageView) {
        CoroutineScope(IO).launch {
            val bitmap: Bitmap? = memcache[imageUrl]
            if (bitmap != null) {
                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                }
            } else {
                val url = URL(imageUrl)
                val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                memcache.put(imageUrl, image)

                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(image)
                }
            }
        }
    }

}