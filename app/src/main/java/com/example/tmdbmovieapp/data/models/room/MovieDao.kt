package com.example.tmdbmovieapp.data.models.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tmdbmovieapp.data.Result

@Dao
interface MovieDao {

    @Insert
    suspend fun insertMovie(res: Result)

    @Query("DELETE FROM MOVIETABLE WHERE id = :id")
    suspend fun deleteByMovieID(id: Int)

    @Query("Select * from MOVIETABLE")
    suspend fun getMovies(): List<Result>

    @Query("Select Exists(Select * from MovieTable where id = :id )")
    suspend fun isLiked(id: Int): Boolean

    @Query("DELETE FROM MovieTable")
    fun nukeTable()

}