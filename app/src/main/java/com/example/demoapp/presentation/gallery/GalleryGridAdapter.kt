package com.example.demoapp.presentation.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.example.demoapp.data.models.Image
import com.example.demoapp.databinding.GridviewItemImageBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class GalleryGridAdapter(private val loadMoreCallback: LoadMoreCallback) :
    RecyclerView.Adapter<GalleryGridAdapter.GalleryViewHolder>() {

    private lateinit var itemActionClickedListener: (image: Image) -> Unit
    private val images = mutableListOf<Image>()
    private val shimmer = Shimmer.ColorHighlightBuilder()
        .setDuration(SHIMMER_DURATION)
        .setBaseAlpha(BASE_ALPHA)
        .setHighlightAlpha(HIGHLIGHT_ALPHA)
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
        if (position >= itemCount - 1)
            loadMoreCallback.onLoadMore()
        val image = images[position]
        with(holder) {
            binding.ivImage.load(image.download_url) {
                size(200, 200)
                allowHardware(true)
                placeholder(getShimmerDrawable())
                memoryCachePolicy(CachePolicy.DISABLED)
                diskCachePolicy(CachePolicy.ENABLED)
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
        notifyItemInserted(this.images.size - 1)
    }

    interface LoadMoreCallback {
        fun onLoadMore()
    }

    companion object {
        private const val SHIMMER_DURATION = 1500L
        private const val BASE_ALPHA = 0.5F
        private const val HIGHLIGHT_ALPHA = 0.6F
    }

}