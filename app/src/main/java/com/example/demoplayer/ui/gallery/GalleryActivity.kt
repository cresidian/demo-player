package com.example.demoplayer.ui.gallery

import GridItemDecoration
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.demoplayer.R
import com.example.demoplayer.adapters.GalleryGridAdapter
import com.example.demoplayer.core.activity.BaseActivity
import com.example.demoplayer.core.extensions.getBitmap
import com.example.demoplayer.core.setVisible
import com.example.demoplayer.databinding.ActivityGalleryBinding
import com.example.demoplayer.models.Image
import com.example.demoplayer.ui.views.TouchImageView
import com.example.demoplayer.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GalleryActivity : BaseActivity() {

    private lateinit var bind: ActivityGalleryBinding
    private val viewModel: GalleryViewModel by viewModels()
    private val adapter = GalleryGridAdapter()
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityGalleryBinding.inflate(layoutInflater)
        val view = bind.root
        setContentView(view)
        init()
        initViewModelObservers()
    }

    private fun init() {
        setSupportActionBar(bind.toolbar)
        supportActionBar?.title = getString(R.string.infinite_gallery)
        bind.rvGallery.addItemDecoration(
            GridItemDecoration(
                context = this,
                R.dimen.grid_item_space
            )
        )
        bind.rvGallery.adapter = adapter
        adapter.setOnItemClickedListener { image ->
            showFullScreenImageDialog(image)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bind.nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                if (scrollY == bind.nestedScrollView.getChildAt(0).measuredHeight - bind.nestedScrollView.measuredHeight) {
                    page++
                    bind.progressBar.setVisible(true)
                    viewModel.getImages(page = page)
                }
            }
        }
        viewModel.getImages(page = page)
    }

    private fun initViewModelObservers() {
        with(viewModel) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewStates.collect { uiState ->
                        when (uiState) {
                            is GalleryViewModel.GalleryViewStates.SetImages -> {
                                adapter.setImages(uiState.images)
                                supportActionBar?.subtitle =
                                    "Page: $page, Images: ${adapter.itemCount}"
                            }
                            is GalleryViewModel.GalleryViewStates.ShowError -> {
                                showToast(uiState.message)
                            }
                            else -> {

                            }
                        }
                    }
                }
            }
        }
    }

    private fun showFullScreenImageDialog(image: Image) {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        val window = dialog.window!!.apply {
            setContentView(layoutInflater.inflate(R.layout.dialog_full_screen_image, null))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawableResource(R.color.colorBlack_50)
        }
        val ivFullImage = window.findViewById<TouchImageView>(R.id.ivFullImage)
        val ibBack = window.findViewById<ImageButton>(R.id.ibBack)
        val ibDownload = window.findViewById<ImageButton>(R.id.ibDownload)
        val ibShare = window.findViewById<ImageButton>(R.id.ibShare)
        ivFullImage.load(image.download_url)
        ibBack.setOnClickListener {
            dialog.cancel()
        }
        ibDownload.setOnClickListener {
            downloadImage(image)
            showToast(getString(R.string.downloading_image))
        }
        ibShare.setOnClickListener {

            Utils.shareImage(this, bitmap = ivFullImage.getBitmap())
        }
        dialog.show()
    }

    private fun downloadImage(image: Image) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri: Uri = Uri.parse(image.download_url)
        val request = DownloadManager.Request(uri)
        val fileExtension = ".jpg"
        request.setTitle(image.id)
        request.setDescription(getString(R.string.downloading_image))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${image.id}$fileExtension"
        )
        downloadManager.enqueue(request)
    }


}