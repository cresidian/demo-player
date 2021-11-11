package com.example.demoplayer.repositories

import com.example.demoplayer.networking.ItunesApi
import com.example.demoplayer.networking.responses.ResponseReceivedListener
import com.example.demoplayer.networking.responses.DemoBackendError
import com.example.demoplayer.networking.responses.SearchResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongsRepositories @Inject constructor(
    private val api: ItunesApi
) {

    suspend fun searchItunes(query: String, callback: ResponseReceivedListener<SearchResponse>) {
        val response = api.search(query)
        if (response.isSuccessful) {
            withContext(Dispatchers.Main){
                callback.onSuccess(response.body()!!)
            }
        } else {
            withContext(Dispatchers.Main){
                callback.onError(DemoBackendError(message = response.message().toString()))
            }
        }
    }

}