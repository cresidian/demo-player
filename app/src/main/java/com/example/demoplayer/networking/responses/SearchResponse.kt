package com.example.demoplayer.networking.responses

import com.example.demoplayer.models.Track
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val resultCount: Int,
    @SerializedName("results") val searchResults: List<Track>
)