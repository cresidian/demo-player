package com.example.demoapp.data.repositories

import com.example.demoapp.core.networking.responses.DemoAppBackendError
import com.example.demoapp.core.networking.responses.ResponseReceivedListener
import com.example.demoapp.core.networking.responses.SearchResponse
import com.example.demoapp.data.sources.remote.Endpoints
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
                callback.onError(DemoAppBackendError(message = response.message().toString()))
            }
        }

    }

}