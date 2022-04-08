package com.example.demoplayer.repositories

import com.example.demoplayer.models.Image
import com.example.demoplayer.networking.Endpoints
import com.example.demoplayer.networking.responses.DemoBackendError
import com.example.demoplayer.networking.responses.ResponseReceivedListener
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
        withContext(Dispatchers.Main) {
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