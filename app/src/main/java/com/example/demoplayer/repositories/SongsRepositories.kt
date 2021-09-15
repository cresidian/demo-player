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

    fun searchItunes(query: String, callback: ResponseReceivedListener<SearchResponse>) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.search(query)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(DemoBackendError(message = response.message().toString()))
                }
            }
        }
    }

}