package dev.ronnieotieno.imageloader

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnieotieno.imageloader.models.ImagesDataClass

class ViewModel : ViewModel() {

    fun fetchPictures(): LiveData<ArrayList<ImagesDataClass>> {
        return ImageRepository().image()
    }
}