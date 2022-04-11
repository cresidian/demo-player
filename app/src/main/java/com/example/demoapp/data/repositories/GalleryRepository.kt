package com.example.demoapp.data.repositories

import com.example.demoapp.core.networking.responses.DemoAppBackendError
import com.example.demoapp.core.networking.responses.ResponseReceivedListener
import com.example.demoapp.data.models.Image
import com.example.demoapp.data.sources.remote.Endpoints
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
        try {
            val response = api.getImages(page, limit)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body()!!)
                    } else {
                        callback.onError(DemoAppBackendError(message = response.message().toString()))
                    }
                } catch (error: IOException) {
                    callback.onError(DemoAppBackendError(message = error.message.toString()))
                }
            }
        } catch (exception: Exception) {
            callback.onError(DemoAppBackendError(message = exception.message.toString()))
        }
    }
}