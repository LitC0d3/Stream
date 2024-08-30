package com.newcode.stream

import retrofit2.Response
import retrofit2.http.GET

interface Endpoints {
    @GET("LitC0d3/demo/db")
    suspend fun getDataMovies(): Response<MovieResponse>
}