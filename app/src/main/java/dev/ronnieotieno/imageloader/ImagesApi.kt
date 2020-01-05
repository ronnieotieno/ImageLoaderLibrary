package dev.ronnieotieno.imageloader

import dev.ronnieotieno.imageloader.models.ImagesDataClass
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ImagesApi {

    @GET("raw/wgkJgazE")
    fun getPictures(

    ): Call<List<ImagesDataClass>>

    companion object {
        operator fun invoke(

        ): ImagesApi {
            return Retrofit.Builder()
                .baseUrl("https://pastebin.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ImagesApi::class.java)
        }
    }
}