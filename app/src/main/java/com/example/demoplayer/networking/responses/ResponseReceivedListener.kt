package com.example.demoplayer.networking.responses

interface ResponseReceivedListener<T> {
    fun onError(error: DemoBackendError)
    fun onSuccess(response: T)
}