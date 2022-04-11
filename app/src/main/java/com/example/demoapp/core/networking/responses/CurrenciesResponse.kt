package com.example.demoapp.core.networking.responses

data class CurrenciesResponse(
    val currencies: Map<String, String>,
    val privacy: String,
    val success: Boolean,
    val terms: String
)