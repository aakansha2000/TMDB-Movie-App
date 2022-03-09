package com.example.tmdbmovieapp.api

import com.example.tmdbmovieapp.data.models.MoviesDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/{category}?api_key=55157ca6962686fb13267f7f9342c2d5")
    suspend fun getMovieList(
        @Path("category") category: String?,
        @Query("language") language: String,
        @Query("page") page: Int?,
    ): Response<MoviesDetails>
}
