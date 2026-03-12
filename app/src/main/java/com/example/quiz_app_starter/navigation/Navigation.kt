package com.example.quiz_app_starter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quiz_app_starter.MainMenuScreen
import com.example.quiz_app_starter.presentation.QuestionScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main_menu"
    ) {
        composable(route = "main_menu") {
            MainMenuScreen(
                bestScore = 12,
                modifier = Modifier
            )
        }

        composable(route = "question_screen") {
            QuestionScreen()
        }
    }
}