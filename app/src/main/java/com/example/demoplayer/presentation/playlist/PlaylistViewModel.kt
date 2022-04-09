package com.example.demoplayer.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoplayer.data.models.Track
import com.example.demoplayer.core.networking.responses.DemoBackendError
import com.example.demoplayer.core.networking.responses.ResponseReceivedListener
import com.example.demoplayer.core.networking.responses.SearchResponse
import com.example.demoplayer.data.repositories.SongsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val repository: SongsRepository
) : ViewModel() {

    private val _viewStates: MutableStateFlow<PlaylistViewStates> = MutableStateFlow(
        PlaylistViewStates.Empty
    )
    val viewStates: StateFlow<PlaylistViewStates> = _viewStates

    sealed class PlaylistViewStates {
        object Empty : PlaylistViewStates()
        data class ShowLoad(val isLoad: Boolean) : PlaylistViewStates()
        data class SetTracks(val tracks: List<Track>) : PlaylistViewStates()
        data class ShowError(val message: String) : PlaylistViewStates()
    }

    fun search(query: String) {
        _viewStates.value = PlaylistViewStates.ShowLoad(true)
        viewModelScope.launch(Dispatchers.IO){
            repository.searchItunes(query, object : ResponseReceivedListener<SearchResponse> {
                override fun onSuccess(response: SearchResponse) {
                    _viewStates.value = PlaylistViewStates.SetTracks(response.searchResults)
                    _viewStates.value = PlaylistViewStates.ShowLoad(false)
                }

                override fun onError(error: DemoBackendError) {
                    _viewStates.value = PlaylistViewStates.ShowLoad(false)
                }
            })
        }
    }

}