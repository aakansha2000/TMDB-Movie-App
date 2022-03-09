package com.example.tmdbmovieapp.di

import android.content.Context
import androidx.room.Room
import com.example.tmdbmovieapp.BuildConfig
import com.example.tmdbmovieapp.api.MovieService
import com.example.tmdbmovieapp.data.models.room.MovieDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    fun provideHttpInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }
    @Singleton
    @Provides
    fun provideHttpClient(
        interceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        val clientBuilder = OkHttpClient
            .Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(interceptor)
        }

        return clientBuilder.build()
    }
    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): MovieService = retrofit.create(MovieService::class.java)

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context.applicationContext,
        MovieDB::class.java,
        "movieDB"
    ).build()

    @Provides
    @Singleton
    fun provideMovieDao(movieDb: MovieDB) = movieDb.movieDao()

}