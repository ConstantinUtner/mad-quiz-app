package com.example.quiz_app_starter.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiz_app_starter.ui.theme.QuizappstarterTheme

/**
 * Composable function that displays the finish screen of the quiz app.
 *
 * Shows a "Game Over" message, the final score, and buttons to restart or exit.
 *
 * @param score The final score achieved by the user.
 * @param onRestart Lambda invoked when the restart button is clicked.
 * @param onExit Lambda invoked when the home button is clicked.
 */
@Composable
fun FinishScreen(
    score: Int = 0,
    onRestart: () -> Unit = {},
    onExit: () -> Unit ={}
) {
    //Root surface
    /*
        Difference Scaffold and Surface:
        Both are used for UI structuring, but Surface provides a material surface,
        e.g. like a card-like element (smaller, more focused), wrapping a part of the UI with a background
        while Scaffold provides a Framework layout, implementing basic material and
        managing common UI components (like Home Screen, App with Bars, persistent UI components...
     */
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        //Columnized area for the content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Game Over Text element
            Text(
                text = "Game Over!",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))
            //Box with Text "Score" and argumented score inside
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Score:",
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            //Row of button-elements for navigating to QuestionScreen/MainMenuScreen
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Restart Button
                FilledIconButton(
                    onClick = onRestart,
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    //Icon for Refresh to navigate to QuestionScreen
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Restart",
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Home Button
                FilledIconButton(
                    onClick = onExit,
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    //Icon for Home to navigate to MainMenuScreen
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinishScreenPreview() {
    QuizappstarterTheme {
        FinishScreen(
            score = 1,
            onRestart = {},
            onExit = {}
        )
    }
}