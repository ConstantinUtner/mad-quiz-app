package com.example.quiz_app_starter.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApiService {

    // Beispiel-URL: https://the-trivia-api.com/api/questions?limit=10&categories=science,history
    @GET("api/questions")
    suspend fun getQuestions(
        @Query("limit") limit: Int = 10,
        @Query("categories") categories: String? = null
    ): List<QuestionDto>

    companion object {
        const val BASE_URL = "https://the-trivia-api.com/"
    }
}