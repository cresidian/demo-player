package com.example.demoapp.core.networking.responses

data class CurrencyLayerBackendError(
    val status: Boolean,
    val errorApp: DemoAppBackendError
)