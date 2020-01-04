package dev.ronnieotieno.imageloader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        getImages()
    }

    private fun getImages() {
        viewModel.fetchPictures().observe(this, Observer {
            val adapter = RecyclerViewAdapter(this, it)
            recyclerView.layoutManager = GridLayoutManager(this, 2)
            recyclerView.adapter = adapter

        })
    }
}
