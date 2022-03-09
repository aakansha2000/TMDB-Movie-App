package com.example.tmdbmovieapp.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tmdbmovieapp.R
import com.example.tmdbmovieapp.data.Result
import com.example.tmdbmovieapp.data.models.MoviesDetails
import kotlinx.android.synthetic.main.child_rv_layout.view.*

class MovieAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var mcontext: Context
    private var items: List<Result> = ArrayList()
    private lateinit var movieList: MoviesDetails

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.child_rv_layout, parent, false),
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                holder.bind(items.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(movieList: List<Result>) {
        Log.d("kuchdekhle", "submitLIst caled $movieList")
        items = movieList
    }

    class MovieViewHolder constructor(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        val movieName = itemView.movie_title

        // val movieDate = itemView.released_date
        val movieImage = itemView.movie_image
        val container = itemView.movie_container

        fun bind(resObj: Result) {
            container.setOnClickListener {
                val intent = Intent(it.context, MovieDetailActivity::class.java)
                intent.putExtra("resultObject", resObj)
                it.context.startActivity(intent)
            }

            movieName.setText(resObj.title)
            //  movieDate.setText(resObj.release_date)
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load("https://image.tmdb.org/t/p/w342${resObj.poster_path}").into(movieImage)
        }
    }
}
