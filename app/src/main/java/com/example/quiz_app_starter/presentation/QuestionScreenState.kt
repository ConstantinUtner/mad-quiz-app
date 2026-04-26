package com.example.quiz_app_starter.model

data class QuestionScreenState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOption: String? = null,
    val timeLeft: Int = 30,
    val isAnswered: Boolean = false,
    val showDialog: Boolean = false,
    val isCorrect: Boolean = false,
    val timeup: Boolean = false,
    val totalCorrectAnswers: Int = 0
) {
    // currentQuestion wird aus Index + Liste berechnet
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)

    val isLastQuestion: Boolean
        get() = currentQuestionIndex == questions.size - 1
}