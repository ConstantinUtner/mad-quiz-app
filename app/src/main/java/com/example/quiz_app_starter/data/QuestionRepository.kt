package com.example.quiz_app_starter.data

import com.example.quiz_app_starter.data.local.QuestionDao
import com.example.quiz_app_starter.data.remote.TriviaApiService
import com.example.quiz_app_starter.data.remote.toDomain
import com.example.quiz_app_starter.data.remote.toEntity
import com.example.quiz_app_starter.model.Question
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(
    private val api: TriviaApiService,
    private val dao: QuestionDao
) {

    /**
     * Versucht zuerst, Fragen von der API zu laden und sie zu cachen.
     * Wenn die API fehlschlägt (kein Netz, Timeout, ...) → fällt auf den
     * lokalen Cache zurück.
     */
    suspend fun getQuestions(
        limit: Int = 10,
        categories: List<String>? = null
    ): List<Question> {
        return try {
            val remote = api.getQuestions(
                limit = limit,
                categories = categories?.joinToString(",")
            )
            val entities = remote.map { it.toEntity() }
            dao.insertQuestions(entities)              // Cache aktualisieren
            entities.map { it.toDomain() }
        } catch (e: Exception) {
            android.util.Log.e("QuestionRepo", "API failed, falling back to cache", e)
            dao.getRandomQuestions(limit).map { it.toDomain() }
        }
    }
}