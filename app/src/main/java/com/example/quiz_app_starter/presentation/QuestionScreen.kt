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
    questions: List<Question> = getDummyQuestions(),
    initialIndex: Int = 0,
    onQuizFinished: (Int) -> Unit = {},
    onMainMenuClick: () -> Unit = {}
) {
    // TODO: Refactor QuestionScreen to consume state from QuestionScreenViewModel.
    // TODO: Use DisposableEffect to attach a LifecycleOwner to the ViewModel (DefaultLifecycleObserver).

    //Tracks current question, index and total correct answers for the score
    var currentQuestionIndex by remember { mutableIntStateOf(initialIndex) }
    val currentQuestion = questions.getOrNull(currentQuestionIndex)
    var totalCorrectAnswers by remember { mutableIntStateOf(0) }

    //Initializes/tracks total-time & timeLeft and answer-state
    val totalTime = 30
    var timeLeft by remember { mutableStateOf(totalTime) }
    var isAnswered by remember { mutableStateOf(false) }
    val lastQuestion = questions.size - 1

    //Calculation & tracking of progress and if time is up
    val progress = timeLeft.toFloat() / totalTime.toFloat()
    var timeup by remember { mutableStateOf(false) }

    //Setting correctAnswer for current question,
    //tracking the option-state and dialog-state
    val correctAnswer = currentQuestion?.correctAnswer
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isCorrect by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    /**
     * Effect that runs every second to decrement the timer.
     * When time runs out, sets timeup to true.
     */
    LaunchedEffect(key1 = timeLeft, key2 = isAnswered) {
        if (timeLeft > 0 && !isAnswered) {
            delay(1000L)
            timeLeft--
        } else if (timeLeft == 0 && !isAnswered) {
            timeup = true
        }
    }

    /**
     * Function to move to the next question.
     * If the last question has been reached, it ends the quiz.
     */
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
            //If end of quiz - invole callback with score argument
            showDialog = false
            onQuizFinished(totalCorrectAnswers)
        }
    }

    /**
     * Dialog displayed when time runs out without an answer.
     * Shows the correct answer.
     */
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

    /**
     * Dialog shown after an answer is selected.
     * Indicates whether the answer was correct or wrong.
     */
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

    //Main Layout scaffold containing the app bar, content and bottom bar
    Scaffold(
        //Displaying remaining time in top bar
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
        //displaying the button for proceeding to the next question
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
        //Content area for question, answers and the progress bar
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            //progress bar indicating remaining time
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Display question/answers if available
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

                //LazyColumn list for answer options
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    //Each answer is rendered as a card module, with a row of answer-text and radio-button
                    items(currentQuestion.answers) { answer ->
                        AnswerCard(
                            answer = answer,
                            //Abfrage ob selectedOption die Answer ist --> Ergebnis: true oder false
                            isSelected = (selectedOption == answer),
                            //Hier passiert eine Zuweisung der selectedOption
                            onSelect = {
                                selectedOption = answer
                                Log.d("QuizApp", "Current Selection: $selectedOption")
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
        QuestionScreen()
    }
}
