package com.example.demoplayer.repositories

import com.example.demoplayer.networking.Endpoints
import com.example.demoplayer.networking.responses.ConversionResponse
import com.example.demoplayer.networking.responses.CurrenciesResponse
import com.example.demoplayer.networking.responses.DemoBackendError
import com.example.demoplayer.networking.responses.ResponseReceivedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val api: Endpoints
) {
    suspend fun getCurrenciesList(
        accessToken: String,
        callback: ResponseReceivedListener<CurrenciesResponse>
    ) {
        val response = api.getCurrenciesList(accessToken)
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

    suspend fun getCurrencyConversions(
        accessToken: String,
        sourceCurrency: String,
        callback: ResponseReceivedListener<ConversionResponse>
    ) {
        val response = api.getLiveConversions(accessToken, sourceCurrency)
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