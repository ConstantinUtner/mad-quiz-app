package com.example.quiz_app_starter.presentation

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz_app_starter.model.Question
import com.example.quiz_app_starter.model.QuestionScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Das ViewModel erhält die Fragen über den Konstruktor
class QuestionScreenViewModel(
    private val initialQuestions: List<Question>
) : ViewModel(), DefaultLifecycleObserver {

    // Privater MutableStateFlow: Nur das ViewModel kann den State verändern
    private val _uiState = MutableStateFlow(QuestionScreenState(questions = initialQuestions))

    // Öffentlicher StateFlow: Die UI kann den State nur lesen (immutable)
    val uiState: StateFlow<QuestionScreenState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private val totalTime = 30

    init {
        startTimer()
    }

    private fun startTimer() {
        // Wir nutzen den viewModelScope. Dieser wird automatisch gecancelt,
        // wenn das ViewModel zerstört wird.
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeft > 0 && !_uiState.value.isAnswered) {
                delay(1000L)
                _uiState.update { currentState ->
                    val newTime = currentState.timeLeft - 1
                    currentState.copy(
                        timeLeft = newTime,
                        timeup = newTime == 0
                    )
                }
            }
        }
    }

    // --- Lifecycle Events ---

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // Timer nur fortsetzen, wenn noch Zeit übrig ist und nicht geantwortet wurde
        if (_uiState.value.timeLeft > 0 && !_uiState.value.isAnswered) {
            startTimer()
        }
    }

    // Wird aufgerufen, wenn die Activity in den Hintergrund rückt
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        timerJob?.cancel() // Timer pausieren
    }

    // --- Events von der UI ---

    fun onOptionSelected(answer: String) {
        _uiState.update { it.copy(selectedOption = answer) }
    }

    fun onSubmit() {
        val currentState = _uiState.value
        val correctAns = currentState.currentQuestion?.correctAnswer

        timerJob?.cancel() // Timer stoppen bei Abgabe

        _uiState.update {
            it.copy(
                isAnswered = true,
                isCorrect = (it.selectedOption == correctAns),
                showDialog = true
            )
        }
    }

    fun onNextQuestion() {
        val currentState = _uiState.value
        val isCorrect = currentState.isCorrect

        _uiState.update {
            it.copy(
                currentQuestionIndex = it.currentQuestionIndex + 1,
                timeLeft = totalTime,
                isAnswered = false,
                selectedOption = null,
                showDialog = false,
                timeup = false,
                totalCorrectAnswers = if (isCorrect) it.totalCorrectAnswers + 1 else it.totalCorrectAnswers
            )
        }
        startTimer() // Timer für die nächste Frage neu starten
    }

    fun onDialogDismissed() {
        _uiState.update { it.copy(showDialog = false, timeup = false) }
    }
}