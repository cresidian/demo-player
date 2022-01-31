package com.example.demoplayer.repositories

import com.example.demoplayer.networking.IApi
import com.example.demoplayer.networking.responses.ConversionResponse
import com.example.demoplayer.networking.responses.CurrenciesResponse
import com.example.demoplayer.networking.responses.DemoBackendError
import com.example.demoplayer.networking.responses.ResponseReceivedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val api: IApi
) {
    suspend fun getCurrenciesList(
        accessToken: String,
        callback: ResponseReceivedListener<CurrenciesResponse>
    ) {
        withContext(Dispatchers.Main) {
            try {
                val response = api.getCurrenciesList(accessToken)
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
        withContext(Dispatchers.Main) {
            try {
                val response = api.getLiveConversions(accessToken, sourceCurrency)
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