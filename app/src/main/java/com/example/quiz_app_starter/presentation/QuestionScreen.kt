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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
    currentQuestionIndex: Int = 0
) {
    val currentQuestion = questions.getOrNull(currentQuestionIndex)
    val totalTime = 30
    var timeLeft by remember { mutableStateOf(totalTime) }
    val progress = timeLeft.toFloat() / totalTime.toFloat()

    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
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

                    IconButton(onClick = { /* Noch keine Funktion */ }) {
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
                    onClick = { /* Noch keine Funktion */ },
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
                var selectedOption by remember { mutableStateOf<String?>(null)}
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