package com.example.tmdbmovieapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbmovieapp.data.models.room.MovieDao
import com.example.tmdbmovieapp.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieDao: MovieDao
) : ViewModel() {

    private val _movieDetails = MutableLiveData<Result>()
    val moviedetails: LiveData<Result> = _movieDetails

    private val _isLiked = MutableLiveData<Boolean>(false)
    val isLiked: LiveData<Boolean> = _isLiked

    fun setMovieDetails(result: Result) {
        _movieDetails.postValue(result)
        viewModelScope.launch {
            val isFav = movieDao.isLiked(result.id)
            _isLiked.postValue(isFav)
        }
    }

    fun onLikeButtonPressed() {
        _movieDetails.value?.let {
            viewModelScope.launch {
                val isFav = movieDao.isLiked(it.id)
                if (isFav) {
                    movieDao.deleteByMovieID(it.id)
                } else {
                    movieDao.insertMovie(it)
                }
                _isLiked.postValue(!isFav)
            }
        }
    }


}
