package com.example.demoapp.presentation.gallery

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.demoapp.R
import com.example.demoapp.core.activity.BaseActivity
import com.example.demoapp.core.extensions.setVisible
import com.example.demoapp.databinding.ActivityGalleryBinding
import com.example.demoapp.presentation.views.GridItemDecoration
import com.example.demoapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GalleryActivity : BaseActivity() {

    private lateinit var bind: ActivityGalleryBinding
    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var adapter: GalleryGridAdapter
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
        adapter = GalleryGridAdapter(object : GalleryGridAdapter.LoadMoreCallback {
            override fun onLoadMore() {
                page++
                bind.progressBar.setVisible(true)
                viewModel.getImages(page = page)
            }
        })
        bind.rvGallery.setHasFixedSize(false)
        bind.include.tvTitle.text = getString(R.string.infinite_gallery)
        bind.rvGallery.addItemDecoration(
            GridItemDecoration(
                context = this,
                R.dimen.grid_item_space
            )
        )
        bind.rvGallery.adapter = adapter
        adapter.setOnItemClickedListener { image ->
            Utils.showFullScreenImageDialog(this, image)
        }
        bind.progressBar.setVisible(true)
        viewModel.getImages(page = page)
    }

    private fun initViewModelObservers() {
        with(viewModel) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewStates.collect { uiState ->
                        when (uiState) {
                            is GalleryViewModel.GalleryViewStates.SetImages -> {
                                bind.progressBar.setVisible(false)
                                adapter.setImages(uiState.images)
                                bind.include.tvSubtitle.text =
                                    "${getString(R.string.page)}: $page, ${getString(R.string.images)}: ${adapter.itemCount}"
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


}