package dev.ronnieotieno.imageloader.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.ronnieotieno.imageloader.models.ImagesDataClass
import dev.ronnieotieno.imageloader.repository.ImageRepository

class ImagesViewModel : ViewModel() {

    private lateinit var imagesliveData: MutableLiveData<ArrayList<ImagesDataClass>>
    private lateinit var imageRepository: ImageRepository

    fun init() {

        if (::imagesliveData.isInitialized) {
            if (imagesliveData != null) {
                return
            }
        }
        imageRepository = ImageRepository().getInstance()!!
        imagesliveData = imageRepository.getImagesFromApi()

    }

    fun getLivedataFromViewModel(): LiveData<ArrayList<ImagesDataClass>> {

        return imagesliveData

    }
}