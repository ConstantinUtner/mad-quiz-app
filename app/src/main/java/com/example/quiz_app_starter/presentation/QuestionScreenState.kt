package com.example.quiz_app_starter.presentation

import com.example.quiz_app_starter.model.Question

/**
 * Data class that encapsulates the state of the QuestionScreen.
 */
data class QuestionScreenState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOption: String? = null,
    val timeLeft: Int = 30,
    val totalCorrectAnswers: Int = 0,
    val isAnswered: Boolean = false,
    val showDialog: Boolean = false,
    val dialogMessage: String = "",
    val isCorrect: Boolean = false,
    val timeUp: Boolean = false,
    val isQuizFinished: Boolean = false
) {
    // Derived property for the current question
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)

    // Derived property for progress (0.0 to 1.0)
    val timerProgress: Float
        get() = timeLeft.toFloat() / 30f
}
