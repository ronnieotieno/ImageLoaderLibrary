package dev.ronnieotieno.imageloader.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.ronnieotieno.imageloader.adapter.ImagesAdapter
import dev.ronnieotieno.imageloader.R
import dev.ronnieotieno.imageloader.viewmodel.ImagesViewModel
import kotlinx.android.synthetic.main.activity_main.*

class ImagesActivity : AppCompatActivity() {

    lateinit var imagesViewModel: ImagesViewModel
    lateinit var recyleriew: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyleriew = recyclerView

        imagesViewModel = ViewModelProviders.of(this).get(ImagesViewModel::class.java)
        imagesViewModel.init()

        observeAndSetImagesToRecyclerview()
    }


    fun observeAndSetImagesToRecyclerview() {
        imagesViewModel.getLivedataFromViewModel().observe(this, Observer {
            val adapter =
                ImagesAdapter(this, it)
            recyclerView.layoutManager = GridLayoutManager(this, 2)
            recyclerView.adapter = adapter

        })
    }
}
