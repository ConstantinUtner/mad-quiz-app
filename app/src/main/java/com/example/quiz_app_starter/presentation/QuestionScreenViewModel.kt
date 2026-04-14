package com.example.quiz_app_starter.presentation

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz_app_starter.model.Question
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * TODO: Exercise - QuestionScreenViewModel
 * 1. Create QuestionScreenViewModel inheriting from ViewModel and DefaultLifecycleObserver.
 * 2. Pass List<Question> to the constructor.
 * 3. Initialize UI state using MutableStateFlow with QuestionScreenState.
 * 4. Expose UI state as an immutable StateFlow.
 * 5. Implement logic to:
 *    - Update selected answer.
 *    - Handle submission and check correctness.
 *    - Manage timer progress.
 *    - Transition to next question or finish quiz.
 *
 * TODO: Exercise - LifecycleObserver
 * 1. Override onStart to resume the timer.
 * 2. Override onPause to pause the timer.
 */
class QuestionScreenViewModel(
    private val questions: List<Question>
) : ViewModel(), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(QuestionScreenState(questions = questions))
    val uiState: StateFlow<QuestionScreenState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // Resume the timer when the app comes to the foreground
        startTimer()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        // Pause the timer when the app goes to the background
        stopTimer()
    }

    /**
     * Starts or resumes the countdown timer.
     */
    private fun startTimer() {
        // Don't start if already answered or if no questions left
        if (uiState.value.isAnswered || uiState.value.timeUp || uiState.value.isQuizFinished) return

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive && uiState.value.timeLeft > 0) {
                delay(1000L)
                _uiState.update { it.copy(timeLeft = it.timeLeft - 1) }
            }
            if (uiState.value.timeLeft <= 0) {
                onTimeUp()
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    /**
     * Handles the event when the timer reaches zero.
     */
    private fun onTimeUp() {
        _uiState.update { 
            it.copy(
                timeUp = true, 
                showDialog = true,
                isAnswered = true,
                dialogMessage = "Time is up! The correct answer was: ${it.currentQuestion?.correctAnswer}"
            ) 
        }
    }

    /**
     * Updates the selected answer option.
     */
    fun onOptionSelected(option: String) {
        if (uiState.value.isAnswered) return
        _uiState.update { currentState ->
            currentState.copy(selectedOption = option)
        }
    }

    /**
     * Submits the current selection and checks if it is correct.
     */
    fun submitAnswer() {
        val currentState = uiState.value
        val selection = currentState.selectedOption ?: return
        val correct = currentState.currentQuestion?.correctAnswer

        stopTimer()

        val isCorrect = selection == correct
        val newScore = if (isCorrect) {
            currentState.totalCorrectAnswers + 1
        } else currentState.totalCorrectAnswers
        val message = if (isCorrect) "Correct!" else "Wrong! The correct answer was: $correct"

        _uiState.update {
            it.copy(
                isAnswered = true,
                isCorrect = isCorrect,
                totalCorrectAnswers = newScore,
                showDialog = true,
                dialogMessage = message
            )
        }
    }

    /**
     * Moves to the next question or finishes the quiz if no more questions are available.
     */
    fun nextQuestion() {
        val currentState = uiState.value
        if (currentState.currentQuestionIndex < questions.size - 1) {
            _uiState.update {
                it.copy(
                    currentQuestionIndex = it.currentQuestionIndex + 1,
                    selectedOption = null,
                    timeLeft = 30,
                    isAnswered = false,
                    showDialog = false,
                    isCorrect = false,
                    timeUp = false
                )
            }
            startTimer()
        } else {
            _uiState.update { it.copy(isQuizFinished = true, showDialog = false) }
        }
    }
}
