package com.example.quiz_app_starter.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quiz_app_starter.model.Question
import com.example.quiz_app_starter.model.getDummyQuestions
import com.example.quiz_app_starter.ui.theme.QuizappstarterTheme
import kotlinx.coroutines.delay

/**
 * Composable function for the Quiz Question Screen.
 *
 * Displays the current question, answer options, a timer, progress bar,
 * and manages the logic for answering questions and completing the quiz.
 *
 * @param questions List of questions for the quiz. Defaults to dummy questions.
 * @param initialIndex Starting index for the question. Defaults to 0.
 * @param onQuizFinished Callback invoked when the quiz is finished, passing the total correct answers.
 * @param onMainMenuClick Callback invoked when the user clicks the main menu button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    viewModel: QuestionScreenViewModel,
    onQuizFinished: (Int) -> Unit = {},
    onMainMenuClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // 2. Lifecycle Observer (Attach the timer)
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = viewModel
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // When the ViewModel sets isQuizFinished to true, trigger the callback
    LaunchedEffect(state.isQuizFinished) {
        if (state.isQuizFinished) {
            onQuizFinished(state.totalCorrectAnswers)
        }
    }

    val lastQuestion = state.questions.size - 1

    //Timeleft is now tracked and calculated in QuestionScreenViewModel

    //GoToNextQuestion is now tracked/done by QuestionScreenViewModel

    /**
     * Dialog displayed when time runs out without an answer.
     * Shows the correct answer.
     */
    if (state.showDialog) {
        LaunchedEffect(Unit) {
            viewModel.submitAnswer()
        }

        val buttonText = if (state.currentQuestionIndex == lastQuestion) "Finish Quiz" else "Next"
        when (state.isCorrect) {
            true -> {
                AlertDialog(
                    title = { Text("Correct!") },
                    text = {},
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.nextQuestion()
                        }) { Text(buttonText) }
                    },
                    onDismissRequest = {}
                )
            }
            false -> {
                AlertDialog(
                    title = { Text("Wrong!") },
                    text = { Text("The correct answer was: ${state.currentQuestion?.correctAnswer}") },
                    confirmButton = {
                        TextButton(onClick = { viewModel.nextQuestion() }) { Text(buttonText) }
                    },
                    onDismissRequest = {}
                )
            }
        }
    }

    //Main Layout scaffold containing the app bar, content and bottom bar
    Scaffold(
        //Displaying remaining time in top bar
        topBar = {
            TopAppBar(
                title = { Text("Quiz App") },
                actions = {
                    Text(
                        text = String.format("00:%02d", state.timeLeft),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp)
                    )

                    IconButton(onClick = { onMainMenuClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        //displaying the button for proceeding to the next question
        bottomBar = {
            Button(
                onClick = {
                    if (state.selectedOption != null) {
                        viewModel.submitAnswer()
                    }
                },
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
        //Content area for question, answers and the progress bar
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            //progress bar indicating remaining time
            LinearProgressIndicator(
                progress = { state.timerProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Display question/answers if available
            if (state.currentQuestion != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = state.questions[state.currentQuestionIndex].question,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(34.dp))

                //LazyColumn list for answer options
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    //Each answer is rendered as a card module, with a row of answer-text and radio-button
                    items(state.questions[state.currentQuestionIndex].answers) { answer ->
                        AnswerCard(
                            answer = answer,
                            //Abfrage ob state.selectedOption die Answer ist --> Ergebnis: true oder false
                            isSelected = (state.selectedOption == answer),
                            //Hier passiert eine Zuweisung der state.selectedOption
                            onSelect = {
                                viewModel.onOptionSelected(answer)
                                Log.d("QuizApp", "Current Selection: $state.selectedOption")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "QuestionScreenPreview")
@Composable
fun QuestionScreenPreview() {
    QuizappstarterTheme {
        // We create a "dummy" list of questions
        val dummyQuestions = getDummyQuestions()

        // We manually instantiate the ViewModel for the preview
        // Note: This only works if your ViewModel constructor is simple
        val viewModel = QuestionScreenViewModel(
            questions = dummyQuestions
        )

        QuestionScreen(viewModel = viewModel)
    }
}
