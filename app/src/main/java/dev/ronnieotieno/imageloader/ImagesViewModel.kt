package dev.ronnieotieno.imageloader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.ronnieotieno.imageloader.models.ImagesDataClass

class ImagesViewModel : ViewModel() {

    lateinit var liveData: MutableLiveData<ArrayList<ImagesDataClass>>
    lateinit var repository: ImageRepository

    fun init() {

        if(::liveData.isInitialized){
            if (liveData != null){
                return
            }
        }
        repository = ImageRepository().getInstance()!!
        liveData = repository.imageList()

    }

    fun getLivedata(): LiveData<ArrayList<ImagesDataClass>> {

        return liveData

    }
}