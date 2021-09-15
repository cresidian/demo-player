package com.example.demoplayer.networking

import com.example.demoplayer.networking.responses.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {

    companion object {

        //Base Endpoints
        //private const val API_VERSION = "/api/v1"

        //Paths
        private const val PATH_SEARCH = "/search?term=jack+johnson"

    }

    @GET(PATH_SEARCH)
    suspend fun search(
        @Query("term") term: String,
        @Query("limit") limit: String = "25"
    ): Response<SearchResponse>
}