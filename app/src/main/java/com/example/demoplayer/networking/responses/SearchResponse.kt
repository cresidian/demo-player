package com.example.demoplayer.networking.responses

import com.example.demoplayer.networking.models.Track
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<Track>
)