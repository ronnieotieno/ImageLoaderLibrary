package dev.ronnieotieno.imageloader

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.ronnieotieno.imageloader.databinding.PictureLayoutBinding
import dev.ronnieotieno.imageloader.models.ImagesDataClass
import dev.ronnieotieno.imageloaderlibrary.ImageLoader

class RecyclerViewAdapter(var context: Context, var images: List<ImagesDataClass>) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val activityAdapterBinding = DataBindingUtil.inflate<PictureLayoutBinding>(
            LayoutInflater
                .from(parent.context), R.layout.picture_layout, parent, false
        )
        return MyViewHolder(activityAdapterBinding)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val image = images[position]

        val imageLoader = ImageLoader(context)
        imageLoader.load(image.urls.regular, holder.binding.imageView)
        holder.binding.linear.setBackgroundColor(Color.parseColor(image.color))

    }

    class MyViewHolder(public var binding: PictureLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}