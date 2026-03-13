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
import com.example.quiz_app_starter.model.Question
import com.example.quiz_app_starter.model.getDummyQuestions
import com.example.quiz_app_starter.ui.theme.QuizappstarterTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    questions: List<Question> = getDummyQuestions(),
    initialIndex: Int = 0,
    onQuizFinished: (Int) -> Unit = {},
    onMainMenuClick: () -> Unit = {}
) {
    var currentQuestionIndex by remember { mutableIntStateOf(initialIndex) }
    val currentQuestion = questions.getOrNull(currentQuestionIndex)
    var totalCorrectAnswers by remember { mutableIntStateOf(0) }


    val totalTime = 30
    var timeLeft by remember { mutableStateOf(totalTime) }
    var isAnswered by remember { mutableStateOf(false) }
    val lastQuestion = questions.size - 1

    val progress = timeLeft.toFloat() / totalTime.toFloat()
    var timeup by remember { mutableStateOf(false) }

    val correctAnswer = currentQuestion?.correctAnswer
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isCorrect by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = timeLeft, key2 = isAnswered) {
        if (timeLeft > 0 && !isAnswered) {
            delay(1000L)
            timeLeft--
        } else if (timeLeft == 0 && !isAnswered) {
            timeup = true
        }
    }

    val goToNextQuestion = {
        timeup = false
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            timeLeft = totalTime
            isAnswered = false
            selectedOption = null
            showDialog = false
            Log.d("QuizApp", "Correct Answer Count: $totalCorrectAnswers")
        } else {
            showDialog = false
            onQuizFinished(totalCorrectAnswers)
        }
    }

    if (timeup) {
        AlertDialog(
            title = { Text("No answer selected. Time is out.") },
            text = { Text("The correct answer was: $correctAnswer") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    goToNextQuestion()
                }) {
                    if (currentQuestionIndex != lastQuestion) {
                        Text("Next")
                    } else {
                        Text("Finish Quiz")
                    }
                }
            },
            onDismissRequest = {})
    }

    if (showDialog) {
        val buttonText = if (currentQuestionIndex == lastQuestion) "Finish Quiz" else "Next"

        when (isCorrect) {
            true -> {
                AlertDialog(
                    title = { Text("Correct!") },
                    text = {},
                    confirmButton = {
                        TextButton(onClick = {
                            totalCorrectAnswers++
                            goToNextQuestion()
                        }) { Text(buttonText) }
                    },
                    onDismissRequest = {})
            }

            false -> {
                AlertDialog(
                    title = { Text("Wrong!") },
                    text = { Text("The correct answer was: $correctAnswer") },
                    confirmButton = {
                        TextButton(onClick = { goToNextQuestion() }) { Text(buttonText) }
                    },
                    onDismissRequest = {})
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz App") },
                actions = {
                    Text(
                        text = "00:${if (timeLeft > 9) timeLeft else "0$timeLeft"}",
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
        bottomBar = {
            Button(
                onClick = {
                    if (selectedOption != null) {
                        isAnswered = true
                        isCorrect = (selectedOption == correctAnswer)
                        showDialog = true
                    }
                },
                enabled = selectedOption != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                Text("Submit")
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (currentQuestion != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = currentQuestion.question,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(34.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(currentQuestion.answers) { answer ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = answer)

                                RadioButton(
                                    selected = answer == selectedOption,
                                    onClick = {
                                        selectedOption = answer
                                        Log.d("QuizApp", "Gewählte Antwort: $selectedOption")
                                    },
                                )
                            }
                        }
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
        QuestionScreen()
    }
}