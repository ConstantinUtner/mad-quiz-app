package com.example.quiz_app_starter.dataLayer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quiz_app_starter.model.Question


@Entity(tableName = "questions")
data class QuestionEntity(
        @PrimaryKey
        val id: String,
        @ColumnInfo(name = "categories")
        val category: String,
        @ColumnInfo(name = "correct_answers")
        val correctAnswer : String,
        @ColumnInfo(name = "answers")
        val answers : List<String>,
        @ColumnInfo(name = "tags")
        val tags : List<String>,
        @ColumnInfo(name = "questions")
        val question : String,
        @ColumnInfo(name = "types")
        val type : String,
        @ColumnInfo(name = "difficulties")
        val difficulty : String,
        @ColumnInfo(name = "regions")
        val region : List<String>,
        @ColumnInfo(name = "is_niche")
        val isNiche : Boolean
    ){

    /**
     * Converts API DTO directly to the Domain model used by the UI.
     */
    fun toQuestion(): com.example.quiz_app_starter.model.Question {
        return Question(
            category = this.category,
            id = this.id,
            correctAnswer = this.correctAnswer,
            // Combine incorrect and correct answers for the UI list
            answers = this.answers.shuffled(),
            tags = this.tags,
            question = this.question,
            type = this.type,
            difficulty = this.difficulty,
            regions = this.region,
            isNiche = this.isNiche
        )
    }
}