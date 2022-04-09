package com.example.demoplayer.core.networking.responses

data class CurrencyLayerBackendError(
    val status: Boolean,
    val error: DemoBackendError
)