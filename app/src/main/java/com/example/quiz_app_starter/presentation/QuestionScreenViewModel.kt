package com.example.quiz_app_starter.presentation

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.quiz_app_starter.model.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    // private val _uiState = MutableStateFlow(QuestionScreenState(questions = questions))
    // val uiState: StateFlow<QuestionScreenState> = _uiState.asStateFlow()

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // Resume timer logic
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        // Pause timer logic
    }
}
