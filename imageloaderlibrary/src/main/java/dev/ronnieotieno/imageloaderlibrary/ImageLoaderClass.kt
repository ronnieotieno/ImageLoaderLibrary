package dev.ronnieotieno.imageloaderlibrary

import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class ImageLoader {

    fun load(imageUrl: String, imageView: ImageView) {
        CoroutineScope(IO).launch {
            val url = URL(imageUrl)
            val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())

            withContext(Dispatchers.Main) {
                imageView.setImageBitmap(image)
            }
        }
    }
}