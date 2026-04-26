package com.example.quiz_app_starter.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quiz_app_starter.model.Question
import com.example.quiz_app_starter.model.getDummyQuestions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    questions: List<Question> = getDummyQuestions(),
    onQuizFinished: (Int) -> Unit = {},
    onMainMenuClick: () -> Unit = {}
) {
    // 1. ViewModel instanziieren mit einer Factory (da wir Parameter übergeben)
    val viewModel: QuestionScreenViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return QuestionScreenViewModel(questions) as T
            }
        }
    )

    // 2. State aus dem ViewModel abonnieren (reagiert automatisch auf Änderungen)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // 3. Lifecycle Observer registrieren
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(viewModel) // onStart und onPause binden
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(viewModel) // Aufräumen
        }
    }

    val progress = state.timeLeft.toFloat() / 30f

    // --- Dialoge ---
    if (state.timeup) {
        AlertDialog(
            title = { Text("Time is out.") },
            text = { Text("The correct answer was: ${state.currentQuestion?.correctAnswer}") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onDialogDismissed()
                    if (state.isLastQuestion) onQuizFinished(state.totalCorrectAnswers)
                    else viewModel.onNextQuestion()
                }) {
                    Text(if (state.isLastQuestion) "Finish Quiz" else "Next")
                }
            },
            onDismissRequest = { viewModel.onDialogDismissed() }
        )
    }

    if (state.showDialog) {
        val title = if (state.isCorrect) "Correct!" else "Wrong!"
        val text = if (!state.isCorrect) "The correct answer was: ${state.currentQuestion?.correctAnswer}" else ""

        AlertDialog(
            title = { Text(title) },
            text = { if (text.isNotEmpty()) Text(text) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onDialogDismissed()
                    if (state.isLastQuestion) onQuizFinished(state.totalCorrectAnswers + if(state.isCorrect) 1 else 0)
                    else viewModel.onNextQuestion()
                }) {
                    Text(if (state.isLastQuestion) "Finish Quiz" else "Next")
                }
            },
            onDismissRequest = { viewModel.onDialogDismissed() }
        )
    }

    // --- UI Layout ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz App") },
                actions = {
                    Text(
                        text = "00:${if (state.timeLeft > 9) state.timeLeft else "0${state.timeLeft}"}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    IconButton(onClick = onMainMenuClick) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Exit")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { viewModel.onSubmit() },
                enabled = state.selectedOption != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                Text("Submit")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            state.currentQuestion?.let { currentQ ->
                Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text(
                        text = currentQ.question,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(34.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(currentQ.answers) { answer ->
                        AnswerCard(
                            answer = answer,
                            isSelected = (state.selectedOption == answer),
                            onSelect = { viewModel.onOptionSelected(answer) }
                        )
                    }
                }
            }
        }
    }
}