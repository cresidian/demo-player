package com.example.demoplayer.networking

import com.example.demoplayer.networking.responses.ConversionResponse
import com.example.demoplayer.networking.responses.CurrenciesResponse
import com.example.demoplayer.networking.responses.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IApi {

    companion object {
        //Base Endpoints
        //private const val API_VERSION = "/api/v1"

        //Paths
        private const val PATH_SEARCH = "/search"
        private const val PATH_LIST_CURRENCIES = "/list"
        private const val PATH_LIST_CONVERSIONS = "/live"
    }

    @GET(PATH_LIST_CURRENCIES)
    suspend fun getCurrenciesList(
        @Query("access_key") accessToken: String,
    ): Response<CurrenciesResponse>

    @GET(PATH_LIST_CONVERSIONS)
    suspend fun getLiveConversions(
        @Query("access_key") accessToken: String,
        @Query("source") source: String,
        ): Response<ConversionResponse>

    @GET(PATH_SEARCH)
    suspend fun search(
        @Query("term") term: String,
        @Query("limit") limit: String = "25"
    ): Response<SearchResponse>
}