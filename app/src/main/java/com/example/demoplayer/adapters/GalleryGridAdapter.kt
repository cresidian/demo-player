package com.example.demoplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.demoplayer.databinding.GridviewItemImageBinding
import com.example.demoplayer.models.Image

class GalleryGridAdapter(
) : RecyclerView.Adapter<GalleryGridAdapter.GalleryViewHolder>() {

    private lateinit var itemActionClickedListener: (image: Image) -> Unit
    private val images= mutableListOf<Image>()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): GalleryViewHolder {
        val binding =
            GridviewItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val image = images[position]
        with(holder) {
            binding.ivImage.load(image.download_url)
            binding.tvAuthor.text = image.author
            binding.ivImage.setOnClickListener {
                itemActionClickedListener.invoke(image)
            }
        }
    }

    inner class GalleryViewHolder(val binding: GridviewItemImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickedListener(onItemClicked: (image: Image) -> Unit) {
        itemActionClickedListener = onItemClicked
    }

    fun setImages(images: List<Image>) {
        this.images.addAll(images)
        notifyDataSetChanged()
    }
}