package com.example.tmdbmovieapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbmovieapp.api.MovieService
import com.example.tmdbmovieapp.data.Result
import com.example.tmdbmovieapp.data.models.MoviesDetails
import com.example.tmdbmovieapp.data.models.room.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val movieService: MovieService,
    private val movieDao: MovieDao
) : ViewModel() {
    private var newList = mutableListOf<Result>()
    private val _movieList = MutableLiveData<MoviesDetails>()
    val movieList: LiveData<MoviesDetails> = _movieList
    private val _movieDetail = MutableLiveData<List<Result>>()
    val movieDetail: LiveData<List<Result>> = _movieDetail
    private val categoryDefault = MutableLiveData("popular")
    private val page = MutableLiveData(1)

    fun changeCatValue(cat: String) {
        page.value = 1
        newList = mutableListOf<Result>()
        categoryDefault.value = cat
        Log.d("changecatvalue", "changeCatValue: ${categoryDefault.value}")
        getMovieList()
    }

    private fun getMovieList() {
        viewModelScope.launch {
            val response = movieService.getMovieList(categoryDefault.value, "en-US", page.value)
            if (response.isSuccessful && response.body() != null) {
                if (newList.isEmpty()) {
                    newList = response.body()?.results!!
                } else {
                    newList.addAll(response.body()?.results ?: emptyList())
                }
                _movieDetail.postValue(newList)
            }
        }
    }

    fun getFavListFromDB() {
        viewModelScope.launch {
            _movieDetail.postValue(movieDao.getMovies())
        }
    }

    fun getNextPage() {
        page.value = page.value?.plus(1)
        getMovieList()
    }
}
