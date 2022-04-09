package com.example.demoplayer.data.repositories

import com.example.demoplayer.data.models.Image
import com.example.demoplayer.data.sources.remote.Endpoints
import com.example.demoplayer.core.networking.responses.DemoBackendError
import com.example.demoplayer.core.networking.responses.ResponseReceivedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class GalleryRepository @Inject constructor(
    private val api: Endpoints
) {
    suspend fun getImages(
        page: Int,
        limit: Int? = null,
        callback: ResponseReceivedListener<List<Image>>
    ) {
        val response = api.getImages(page, limit)
        withContext(Dispatchers.Main){
            try {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(DemoBackendError(message = response.message().toString()))
                }
            } catch (error: IOException) {
                callback.onError(DemoBackendError(message = error.message.toString()))
            }
        }
    }
}