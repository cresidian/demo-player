package com.example.demoplayer.core.networking.responses

import com.google.gson.annotations.SerializedName

data class ConversionResponse(
    @SerializedName("quotes")
    val conversions: Map<String, Double>,
    val success: Boolean,
    val source:String,
)