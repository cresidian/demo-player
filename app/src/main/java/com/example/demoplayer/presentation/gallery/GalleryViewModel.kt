package com.example.demoplayer.presentation.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoplayer.core.networking.responses.DemoBackendError
import com.example.demoplayer.core.networking.responses.ResponseReceivedListener
import com.example.demoplayer.data.models.Image
import com.example.demoplayer.data.repositories.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: GalleryRepository
) : ViewModel() {

    private val _viewStates: MutableStateFlow<GalleryViewStates> = MutableStateFlow(
        GalleryViewStates.Empty
    )
    val viewStates: StateFlow<GalleryViewStates> = _viewStates

    sealed class GalleryViewStates {
        object Empty : GalleryViewStates()
        data class SetImages(val images: List<Image>) : GalleryViewStates()
        data class ShowError(val message: String) : GalleryViewStates()
    }


    fun getImages(page: Int, limit: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getImages(
                page = page,
                limit = limit,
                object : ResponseReceivedListener<List<Image>> {
                    override fun onSuccess(response: List<Image>) {
                        _viewStates.value = GalleryViewStates.SetImages(response)
                    }

                    override fun onError(error: DemoBackendError) {
                        _viewStates.value = GalleryViewStates.ShowError(error.message)
                    }
                })
        }
    }

}