package dev.ronnieotieno.imageloader.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import dev.ronnieotieno.imageloader.api.ImagesApi
import dev.ronnieotieno.imageloader.models.ImagesDataClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageRepository {

    private var instance: ImageRepository? = null

    fun getInstance(): ImageRepository? {
        if (instance == null) {
            instance = ImageRepository()
        }
        return instance
    }

    fun imageList(): MutableLiveData<ArrayList<ImagesDataClass>>{

        val imagesApi = ImagesApi()

        val pictures = MutableLiveData<ArrayList<ImagesDataClass>>()
        val imageDataList: ArrayList<ImagesDataClass> = arrayListOf()

       imagesApi
            .getPictures().enqueue(object : Callback<List<ImagesDataClass>> {
                override fun onFailure(call: Call<List<ImagesDataClass>>, t: Throwable) {

                    Log.d("ImageRepository", "Throwable $t")

                }

                override fun onResponse(
                    call: Call<List<ImagesDataClass>>,
                    response: Response<List<ImagesDataClass>>
                ) {
                    if (response.isSuccessful) {

                        imageDataList.addAll(response.body()!!)
                        pictures.value = imageDataList
                        Log.d("ImageRepository", "ImagesList ${response.body()}")
                    } else {
                        Log.d("ImageRepository", "ImagesList ${response.errorBody()}")
                    }

                }

            })

        return pictures
    }

}