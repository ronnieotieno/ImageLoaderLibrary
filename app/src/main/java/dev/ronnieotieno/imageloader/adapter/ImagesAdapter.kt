package dev.ronnieotieno.imageloader.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.ronnieotieno.imageloader.ui.ImagesDetailsActivity
import dev.ronnieotieno.imageloader.R
import dev.ronnieotieno.imageloader.databinding.PictureLayoutBinding
import dev.ronnieotieno.imageloader.models.ImagesDataClass
import dev.ronnieotieno.imageloaderlibrary.ImageLoader

class ImagesAdapter(var context: Context, var images: List<ImagesDataClass>) :
    RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val activityAdapterBinding = DataBindingUtil.inflate<PictureLayoutBinding>(
            LayoutInflater
                .from(parent.context),
            R.layout.picture_layout, parent, false
        )
        return MyViewHolder(activityAdapterBinding)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val image = images[position]

        holder.sendData(image)

        val imageLoader = ImageLoader(context)
        imageLoader.load(image.urls?.regular, holder.binding.imageView)
        //holder.binding.cardview.setBackgroundColor(Color.parseColor(image.color))
        holder.binding.creator.text = image.user?.name
        holder.binding.likes.text = "Likes: ${image.likes}"

        image.categories?.get(0)?.id

    }

    inner class MyViewHolder(var binding: PictureLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var imageSend: ImagesDataClass? = null
        fun sendData(image: ImagesDataClass) {

            imageSend = image


        }

        init {

            binding.root.setOnClickListener {

                var category = "Categories: "
                if (imageSend?.categories!!.size > 1) {
                    for (i in imageSend!!.categories!!) {
                        category += "${i.title}, "
                    }
                    category = category.substring(0, category.length - 2)
                } else {
                    category = "Category: ${imageSend?.categories!![0].title}"
                }


                val intent = Intent(context, ImagesDetailsActivity::class.java)
                intent.putExtra("image", imageSend?.urls?.regular)
                intent.putExtra("Creator", imageSend?.user?.name)
                intent.putExtra("likes", "Likes: ${imageSend?.likes}" + "\n" + category)
                intent.putExtra("creatorPic", imageSend?.user?.profile_image?.medium)

                context.startActivity(intent)
            }
        }

    }

}