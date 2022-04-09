package com.example.demoplayer.data.repositories

import com.example.demoplayer.core.networking.responses.DemoBackendError
import com.example.demoplayer.core.networking.responses.ResponseReceivedListener
import com.example.demoplayer.core.networking.responses.SearchResponse
import com.example.demoplayer.data.sources.remote.Endpoints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongsRepository @Inject constructor(
    private val api: Endpoints
) {

    suspend fun searchItunes(query: String, callback: ResponseReceivedListener<SearchResponse>) {
        val response = api.searchSong(query)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                callback.onSuccess(response.body()!!)

            } else {
                callback.onError(DemoBackendError(message = response.message().toString()))
            }
        }

    }

}