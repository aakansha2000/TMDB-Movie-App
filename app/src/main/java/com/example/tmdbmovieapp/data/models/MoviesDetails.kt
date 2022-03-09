package com.example.tmdbmovieapp.data.models

import com.example.tmdbmovieapp.data.Result

data class MoviesDetails(
    val dates: Dates,
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int

    // data class for movie details
)
