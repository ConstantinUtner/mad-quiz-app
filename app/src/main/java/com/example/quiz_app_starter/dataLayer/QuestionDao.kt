package com.example.quiz_app_starter.dataLayer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quiz_app_starter.model.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    //Convenience methods for queries, inserts, updates & deletes
    //e.g. insertQuestions (listOfQuestions), insertQuestion(questionSingular), getRandomQuestions(limiter as integer)

    // Gibt zufällig sortierte Fragen zurück (limitiert)
    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestions(limit: Int): List<QuestionEntity>

    // Eine einzelne Frage einfügen (überschreibt bei gleicher ID)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    // Liste von Fragen einfügen (überschreibt bei gleicher ID)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    // Optional: Tabelle leeren
    @Query("DELETE FROM questions")
    suspend fun clearAll()

}