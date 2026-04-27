package com.example.quiz_app_starter.data.remote

import com.example.quiz_app_starter.data.local.QuestionEntity
import com.example.quiz_app_starter.model.Question

// API → DB
fun QuestionDto.toEntity(): QuestionEntity {
    val allAnswers = (incorrectAnswers + correctAnswer).shuffled()
    return QuestionEntity(
        id = id,
        category = category,
        correctAnswer = correctAnswer,
        answers = allAnswers,
        tags = tags ?: emptyList(),
        question = question,
        type = type,
        difficulty = difficulty,
        regions = regions,
        isNiche = isNiche
    )
}

// DB → Domain (UI-Modell)
fun QuestionEntity.toDomain(): Question = Question(
    id = id,
    category = category,
    correctAnswer = correctAnswer,
    answers = answers,
    tags = tags,
    question = question,
    type = type,
    difficulty = difficulty,
    regions = regions,
    isNiche = isNiche
)