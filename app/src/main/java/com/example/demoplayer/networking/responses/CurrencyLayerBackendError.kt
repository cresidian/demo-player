package com.example.demoplayer.networking.responses

data class CurrencyLayerBackendError(
    val status: Boolean,
    val error: DemoBackendError
)