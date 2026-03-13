package com.example.quiz_app_starter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quiz_app_starter.presentation.FinishScreen
import com.example.quiz_app_starter.presentation.MainMenuScreen
import com.example.quiz_app_starter.presentation.QuestionScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MainMenuScreen.route
    ) {
        composable(route = Screen.MainMenuScreen.route) {
            MainMenuScreen(
                bestScore = 12,
                modifier = Modifier,
                onPlayClick = { navController.navigate(Screen.QuestionScreen.route) }
            )
        }

        composable(route = Screen.QuestionScreen.route) {
            QuestionScreen(
                initialIndex = 9,
                onQuizFinished = { score ->
                    navController.navigate(Screen.FinishScreen.createRoute(score)) {
                        popUpTo("question_screen") { inclusive = true }
                    }
                },
                onMainMenuClick = {
                    navController.popBackStack(Screen.MainMenuScreen.route, inclusive = false)
                }
            )
        }

        composable(
            route = Screen.FinishScreen.route,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0

            FinishScreen(
                score = score,
                onExit = {
                    navController.popBackStack(Screen.MainMenuScreen.route, inclusive = false)
                },
                onRestart = {
                    navController.navigate(Screen.QuestionScreen.route) {
                        popUpTo(Screen.MainMenuScreen.route)
                    }
                }
            )
        }
    }
}