package com.example.quiz_app_starter.dataLayer

import com.example.quiz_app_starter.api.QuestionApiService
import com.example.quiz_app_starter.model.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val questionDao: QuestionDao,
    private val triviaAPI: QuestionApiService
) {
    fun getQuestions(): Flow<List<Question>> = flow {
        try {
            val limit = 10
            val categories = "science, history"

            val apiQuestions = triviaAPI.getQuestions(limit, categories)
            val entities = apiQuestions.map { it.toEntity() }
            questionDao.insertQuestions(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 2. ALWAYS emit from the Local DB (Single Source of Truth)
        // MAP: Entity -> Domain Model (for UI)
        val localEntities = questionDao.getRandomQuestions(10)
        val domainQuestions = localEntities.map { it.toQuestion() }

        emit(domainQuestions)
    }
}