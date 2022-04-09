package com.example.demoplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.example.demoplayer.data.models.Image
import com.example.demoplayer.databinding.GridviewItemImageBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class GalleryGridAdapter(
) : RecyclerView.Adapter<GalleryGridAdapter.GalleryViewHolder>() {

    private lateinit var itemActionClickedListener: (image: Image) -> Unit
    private val images = mutableListOf<Image>()
    private val shimmer = Shimmer.AlphaHighlightBuilder()
        .setDuration(1500)
        .setBaseAlpha(0.8f)
        .setHighlightAlpha(0.6f)
        .setDirection(Shimmer.Direction.TOP_TO_BOTTOM)
        .setAutoStart(true)
        .build()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): GalleryViewHolder {
        val binding =
            GridviewItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val image = images[position]
        with(holder) {
            binding.ivImage.load(image.download_url) {
                diskCachePolicy(CachePolicy.ENABLED)
                placeholder(getShimmerDrawable())
                allowHardware(true)
            }
            binding.tvAuthor.text = image.author
            binding.ivImage.setOnClickListener {
                itemActionClickedListener.invoke(image)
            }
        }
    }

    private fun getShimmerDrawable(): ShimmerDrawable {
        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }
        return shimmerDrawable
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