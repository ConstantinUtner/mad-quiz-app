package com.example.quiz_app_starter.presentation

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz_app_starter.data.QuestionRepository
import com.example.quiz_app_starter.model.QuestionScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionScreenViewModel @Inject constructor(
    private val repository: QuestionRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(QuestionScreenState())
    val uiState: StateFlow<QuestionScreenState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private val totalTime = 30

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            val questions = repository.getQuestions(limit = 10)
            _uiState.update { it.copy(questions = questions) }
            if (questions.isNotEmpty()) startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeft > 0 && !_uiState.value.isAnswered) {
                delay(1000L)
                _uiState.update { current ->
                    val newTime = current.timeLeft - 1
                    current.copy(timeLeft = newTime, timeup = newTime == 0)
                }
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if (_uiState.value.timeLeft > 0 && !_uiState.value.isAnswered) {
            startTimer()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        timerJob?.cancel()
    }

    fun onOptionSelected(answer: String) {
        _uiState.update { it.copy(selectedOption = answer) }
    }

    fun onSubmit() {
        val correctAns = _uiState.value.currentQuestion?.correctAnswer
        timerJob?.cancel()
        _uiState.update {
            it.copy(
                isAnswered = true,
                isCorrect = (it.selectedOption == correctAns),
                showDialog = true
            )
        }
    }

    fun onNextQuestion() {
        val isCorrect = _uiState.value.isCorrect
        _uiState.update {
            it.copy(
                currentQuestionIndex = it.currentQuestionIndex + 1,
                timeLeft = totalTime,
                isAnswered = false,
                selectedOption = null,
                showDialog = false,
                timeup = false,
                totalCorrectAnswers = if (isCorrect) it.totalCorrectAnswers + 1
                else it.totalCorrectAnswers
            )
        }
        startTimer()
    }

    fun onDialogDismissed() {
        _uiState.update { it.copy(showDialog = false, timeup = false) }
    }
}