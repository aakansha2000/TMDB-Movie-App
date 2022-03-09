package com.example.tmdbmovieapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tmdbmovieapp.R
import com.example.tmdbmovieapp.data.models.room.MovieDB
import com.example.tmdbmovieapp.data.Result
import com.example.tmdbmovieapp.viewModels.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movie_detail.*

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {
    private lateinit var derivedObject: Result
    private val movieDetailViewModel: MovieDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        setData()
        // setDBInstance()

        movieDetailViewModel.isLiked.observe(
            this,
            Observer { isLiked ->
                isLiked?.let {
                    if (it) {
                        favButton1.setImageDrawable(
                            AppCompatResources.getDrawable(
                                applicationContext,
                                R.drawable.ic_baseline_favorite_red
                            )
                        )
                    } else {
                        favButton1.setImageDrawable(
                            AppCompatResources.getDrawable(
                                applicationContext,
                                R.drawable.ic_baseline_favorite_24
                            )
                        )
                    }
                }
            }
        )
        favButton1.setOnClickListener {
            movieDetailViewModel.onLikeButtonPressed()
        }
    }

    private fun setData() {
        derivedObject = intent.getSerializableExtra("resultObject") as Result
        movieDetailViewModel.setMovieDetails(derivedObject)
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
        Glide.with(this)
            .applyDefaultRequestOptions(requestOptions)
            .load("https://image.tmdb.org/t/p/w342${derivedObject.backdrop_path}")
            .into(movie_backdrop)
        Glide.with(this)
            .applyDefaultRequestOptions(requestOptions)
            .load("https://image.tmdb.org/t/p/w342${derivedObject.poster_path}")
            .into(movie_poster)
        movie_title.setText(derivedObject.title)
        movie_release_date.setText(derivedObject.release_date)
        movie_overview.setText(derivedObject.overview)
        movie_rating.rating = derivedObject.vote_average / 2f
    }
}
