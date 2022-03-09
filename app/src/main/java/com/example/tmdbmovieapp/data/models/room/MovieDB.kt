package com.example.tmdbmovieapp.data.models.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tmdbmovieapp.data.Result

@Database(entities = [Result::class], version = 2)
abstract class MovieDB : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}
