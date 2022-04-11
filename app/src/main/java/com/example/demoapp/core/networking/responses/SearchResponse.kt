package com.example.demoapp.core.networking.responses

import com.example.demoapp.data.models.Track
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val resultCount: Int,
    @SerializedName("results") val searchResults: List<Track>
)