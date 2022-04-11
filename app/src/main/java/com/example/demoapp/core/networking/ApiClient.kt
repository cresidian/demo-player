package com.example.demoapp.core.networking

import android.content.Context
import com.example.demoapp.BuildConfig
import com.example.demoapp.utils.Utils
import okhttp3.Cache
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

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val onlineCacheInterceptor = Interceptor { chain ->
        val request = chain.proceed(chain.request())
        request.newBuilder().header("Cache-Control", "public, max-age=$CACHE_AGE_DURATION")
            .build()
    }

    private val offlineCacheInterceptor = Interceptor { chain ->
        val request = chain.request()
        if (!Utils.isOnline(applicationContext)) {
            request.newBuilder()
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=$CACHE_AGE_DURATION"
                )
                .build()
        }
        chain.proceed(request)
    }

    fun getApiClient(): Retrofit = Retrofit.Builder()
        .client(createHttpClient())
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun createHttpClient(): OkHttpClient {
        val dispatcher = Dispatcher().apply { maxRequests = MAX_REQUESTS }
        val clientBuilder = OkHttpClient.Builder().apply {
            cache(Cache(applicationContext.cacheDir, CACHE_SIZE_LIMIT))
            readTimeout(REQUEST_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            writeTimeout(REQUEST_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            retryOnConnectionFailure(true)
            pingInterval(PING_INTERVAL, TimeUnit.SECONDS)
            addInterceptor(offlineCacheInterceptor)
            addNetworkInterceptor(onlineCacheInterceptor)

            //addInterceptor(connectivityInterceptor)
            if (BuildConfig.BUILD_TYPE != BUILD_TYPE_RELEASE) {
                addNetworkInterceptor(loggingInterceptor)
            }
            dispatcher(dispatcher)
        }
        return clientBuilder.build()
    }

    companion object {
        private const val CACHE_AGE_DURATION = 1800 //1800 //30 minutes
        private const val CACHE_SIZE_LIMIT = (10 * 1024 * 1024).toLong() //10 mb
        private const val REQUEST_TIMEOUT_MS = 30_000L
        private const val MAX_REQUESTS = 1
        private const val PING_INTERVAL = 5L
        private const val BUILD_TYPE_RELEASE = "release"
    }

}


