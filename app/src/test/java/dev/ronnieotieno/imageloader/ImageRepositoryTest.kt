package dev.ronnieotieno.imageloader

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import dev.ronnieotieno.imageloader.models.ImagesDataClass
import dev.ronnieotieno.imageloader.repository.ImageRepository
import dev.ronnieotieno.imageloader.viewmodel.ImagesViewModel
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

class ImageRepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val imageRepository = Mockito.mock(ImageRepository::class.java)
    val imagesViewModel = ImagesViewModel()

    @Test
    fun testImageLiveData() {

        imageRepository.getInstance()

        val pictures = MutableLiveData<ArrayList<ImagesDataClass>>()
        val imageDataList: ArrayList<ImagesDataClass> = arrayListOf()
        val image = ImagesDataClass()
        image.likes = 4
        for (i in 1..5) {
            imageDataList.add(image)
        }
        pictures.value = imageDataList
        Mockito.`when`(imageRepository.getImagesFromApi())
            .thenReturn(pictures)

        imagesViewModel.init().also {
            imagesViewModel.getLivedataFromViewModel()
        }

        imagesViewModel.imagesliveData.value?.isNotEmpty()?.let { assert(it) }
    }

    @Test
    fun testEmptyImageLiveData() {
        Mockito.`when`(imageRepository.getImagesFromApi())
            .thenReturn(MutableLiveData<ArrayList<ImagesDataClass>>())

        imagesViewModel.init().also {
            imagesViewModel.getLivedataFromViewModel()
        }

        Assert.assertNull(imagesViewModel.imagesliveData.value)


    }

}
