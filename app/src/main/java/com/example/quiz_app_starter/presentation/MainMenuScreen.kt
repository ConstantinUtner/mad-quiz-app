package com.example.quiz_app_starter.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiz_app_starter.R
import com.example.quiz_app_starter.ui.theme.QuizappstarterTheme
import com.example.quiz_app_starter.ui.theme.rainbowColors

/**
 * Displays the main menu screen of the quiz app.
 *
 * Shows the app logo, title, a styled message, the best score achieved so far,
 * and a button to start the quiz.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param bestScore The highest score achieved, displayed prominently.
 * @param onPlayClick Lambda invoked when the "Play!" button is pressed.
 */
@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    bestScore: Int,
    onPlayClick: () -> Unit = {},
) {
    //Scaffolding to provide a basic layout structure
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->

        //The first vertical container for the logo
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Actually displaying the app logo image inside the Column
            Image(
                painter = painterResource(id = R.drawable.quiz_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )

            //Spacer between the Logo and the Title
            Spacer(modifier = Modifier.height(16.dp))

            //Vertically aligned title inside the Column
            Text(
                text = "My Awesome Quiz App",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            //Spacer between the title and the stylized text
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                //A styled text with a rainbow color gradient
                text = buildAnnotatedString {
                    //appending "normal" text before the stylized one
                    append("Test your\n")

                    withStyle(
                        style = SpanStyle(
                            brush = Brush.linearGradient(colors = rainbowColors),
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    ) {
                        append("knowledge!".uppercase())
                    }
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            //Spacer between stylized text and TextBox with score
            Spacer(modifier = Modifier.height(32.dp))

            //Containerbox with column inside for displaying text
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(0.5f))
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Best of all time",
                        color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                    )
                    Text(text = bestScore.toString(), fontSize = 64.sp)
                }
            }

            //Spacer between best score-box and Button
            Spacer(modifier = Modifier.height(24.dp))

            //Play-Button
            Button(
                onClick = onPlayClick,
                modifier = Modifier.fillMaxWidth(0.6f),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Play!", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(showBackground = true, name = "MainMenuPreview")
@Composable
fun MainMenuScreenPreview() {
    QuizappstarterTheme {
        MainMenuScreen(Modifier,12, onPlayClick = {})
    }
}
