package com.example.demoapp.core.networking.responses

interface ResponseReceivedListener<T> {
    fun onError(error: DemoAppBackendError)
    fun onSuccess(response: T)
}