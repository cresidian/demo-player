package com.example.demoplayer.core.networking.responses

interface ResponseReceivedListener<T> {
    fun onError(error: DemoBackendError)
    fun onSuccess(response: T)
}