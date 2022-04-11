package com.example.demoapp.core.networking.responses

import com.google.gson.annotations.SerializedName

data class DemoAppBackendError(
    val code: Int?=null,
    @SerializedName(value = "message", alternate = ["info"])
    val message: String
)