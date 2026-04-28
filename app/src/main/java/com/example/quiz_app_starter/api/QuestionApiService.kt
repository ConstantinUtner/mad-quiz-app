package com.example.quiz_app_starter.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://the-trivia-api.com/api/"

interface QuestionApiService {
    @GET("questions")
    suspend fun getQuestions(
        @Query("limit") limit: Int,
        @Query("categories") categories: String
    ): List<QuestionDto> // Ensure QuestionResponse is also defined
}

object TriviaAPI {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    val gson = GsonBuilder().create()
    val api: QuestionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(QuestionApiService::class.java)
    }
}
