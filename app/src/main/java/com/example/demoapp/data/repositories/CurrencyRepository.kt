package com.example.demoapp.data.repositories

import com.example.demoapp.core.networking.responses.ConversionResponse
import com.example.demoapp.core.networking.responses.CurrenciesResponse
import com.example.demoapp.core.networking.responses.DemoAppBackendError
import com.example.demoapp.core.networking.responses.ResponseReceivedListener
import com.example.demoapp.data.sources.remote.Endpoints
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
                    callback.onError(
                        DemoAppBackendError(
                            message = response.message(),
                            code = response.code()
                        )
                    )
                }
            } catch (error: IOException) {
                callback.onError(DemoAppBackendError(message = error.message.toString()))
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
                    callback.onError(
                        DemoAppBackendError(
                            message = response.message(),
                            code = response.code()
                        )
                    )
                }
            } catch (error: IOException) {
                callback.onError(DemoAppBackendError(message = error.message.toString()))
            }
        }
    }

}