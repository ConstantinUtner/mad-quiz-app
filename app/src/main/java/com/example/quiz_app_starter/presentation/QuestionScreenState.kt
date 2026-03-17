package com.example.quiz_app_starter.presentation

import com.example.quiz_app_starter.model.Question

/**
 * TODO: Exercise - QuestionScreenState
 * Create a data class that encapsulates the state of the QuestionScreen.
 * It should include:
 * - questions: List<Question>
 * - currentQuestionIndex: Int
 * - selectedOption: String?
 * - timeLeft: Int
 * - totalCorrectAnswers: Int
 * - isAnswered: Boolean
 * - showDialog: Boolean
 * - isCorrect: Boolean
 * - timeUp: Boolean
 * 
 * Optional: Derive currentQuestion based on index and questions list.
 */
data class QuestionScreenState(
    // Add properties here
    val questions: List<Question> = emptyList()
)
