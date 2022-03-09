package com.example.tmdbmovieapp.ui

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovieapp.R
import com.example.tmdbmovieapp.Utils.Constants
import com.example.tmdbmovieapp.data.Result
import com.example.tmdbmovieapp.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var FavItems: List<Result>
    private lateinit var movieAdapter: MovieAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerview()

        mainViewModel.changeCatValue("now_playing")
        latest_movies_chip.setOnClickListener(
            View.OnClickListener {
                mainViewModel.changeCatValue("upcoming")
            }
        )
        now_playing_movies.setOnClickListener(
            View.OnClickListener {
                mainViewModel.changeCatValue("now_playing")
            }
        )
        top_rated.setOnClickListener(
            View.OnClickListener {
                mainViewModel.changeCatValue("top_rated")
            }
        )

        favList.setOnClickListener(
            View.OnClickListener {
                mainViewModel.getFavListFromDB()
            }
        )

        mainViewModel.movieDetail.observe(
            this,
            Observer {
                movieAdapter.submitList(it)
                movieAdapter.notifyDataSetChanged()
            }
        )
    }

    fun initRecyclerview() {
        recyclerview.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            layoutManager = GridLayoutManager(context, 3)
            addOnScrollListener(scrollListener)
            movieAdapter = MovieAdapter()
            adapter = movieAdapter
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val notLoadingOrLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = notLoadingOrLastPage && isAtLastItem && isNotAtBeginning &&
                isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                mainViewModel.getNextPage()
                isScrolling = false
            }
        }
    }
}
