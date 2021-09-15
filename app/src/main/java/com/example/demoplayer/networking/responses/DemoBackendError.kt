package com.example.demoplayer.networking.responses

data class DemoBackendError(
    val code: Int = 400,
    val message: String
)