package com.example.quiz_app_starter.api

import com.example.quiz_app_starter.dataLayer.QuestionEntity
import com.example.quiz_app_starter.model.Question

data class QuestionDto(
    val category: String,
    val id: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val tags: List<String>,
    val question: String,
    val type: String,
    val difficulty: String,
    val regions: List<String>? = emptyList(),
    val isNiche: Boolean
) {
    /**
     * Converts API DTO to the Room Entity for local caching.
     */
    fun toEntity(): QuestionEntity {
        return QuestionEntity(
            id = this.id,
            category = this.category,
            correctAnswer = this.correctAnswer,
            // We store the incorrect answers separately or as a list
            answers = this.incorrectAnswers + this.correctAnswer,
            tags = this.tags,
            question = this.question,
            type = this.type,
            difficulty = this.difficulty,
            region = this.regions ?: emptyList(),
            isNiche = this.isNiche
        )
    }
}
