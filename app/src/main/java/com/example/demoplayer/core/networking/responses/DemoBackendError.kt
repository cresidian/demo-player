package com.example.demoplayer.core.networking.responses

import com.google.gson.annotations.SerializedName

data class DemoBackendError(
    val code: Int = 400,
    @SerializedName(value = "message", alternate = ["info"])
    val message: String
)