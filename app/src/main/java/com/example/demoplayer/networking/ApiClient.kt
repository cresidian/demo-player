package com.example.demoplayer.networking

import android.content.Context
import com.example.demoplayer.BuildConfig
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ApiClient @Inject constructor(
    private val applicationContext: Context
) {

    fun getApiClient(): Retrofit = Retrofit.Builder()
        .client(createHttpClient())
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun createHttpClient(): OkHttpClient {
        val dispatcher = Dispatcher().apply { maxRequests = MAX_REQUESTS }
        val clientBuilder = OkHttpClient.Builder().apply {
            readTimeout(REQUEST_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            writeTimeout(REQUEST_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            retryOnConnectionFailure(true)
            pingInterval(PING_INTERVAL, TimeUnit.SECONDS)
            if (BuildConfig.BUILD_TYPE != BUILD_TYPE_RELEASE) {
                addNetworkInterceptor(createHttpLoggingInterceptor())
            }
            dispatcher(dispatcher)
        }
        return clientBuilder.build()
    }

    private fun createHttpLoggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    companion object {
        private const val REQUEST_TIMEOUT_MS = 30_000L
        private const val MAX_REQUESTS = 1
        private const val PING_INTERVAL = 5L
        private const val BUILD_TYPE_RELEASE = "release"
    }

}


