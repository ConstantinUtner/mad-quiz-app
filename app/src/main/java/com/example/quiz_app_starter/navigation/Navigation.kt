package com.example.quiz_app_starter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quiz_app_starter.model.ScoreViewModel
import com.example.quiz_app_starter.presentation.FinishScreen
import com.example.quiz_app_starter.presentation.MainMenuScreen
import com.example.quiz_app_starter.presentation.QuestionScreen

/**
 * Sets up the navigation graph for the quiz app.
 *
 * Defines the different screens (Main Menu, Question, Finish) and manages navigation
 * between them using NavController. Also shares the ScoreViewModel across screens.
 */
@Composable
fun Navigation() {
    val navController = rememberNavController()

    //instantiating the ViewModel to share score data between screens and handle calculation elsewhere
    val scoreViewModel: ScoreViewModel = viewModel()

    //NavHost to define the navigation graph with startomg destination
    NavHost(
        navController = navController,
        startDestination = Screen.MainMenuScreen.route
    ) {
        //First screen: Main Menu Composable
        composable(route = Screen.MainMenuScreen.route) {
            MainMenuScreen(
                bestScore = scoreViewModel.bestScore,
                modifier = Modifier,
                onPlayClick = { navController.navigate(Screen.QuestionScreen.route) }
            )
        }

        //Second screen: QuizScreen Composable
        composable(route = Screen.QuestionScreen.route) {
            QuestionScreen(
                onQuizFinished = { score ->
                    scoreViewModel.updateScore(score)
                    //Navigate to finishScreen with score argument when clicking the button
                    navController.navigate(Screen.FinishScreen.createRoute(score)) {
                        popUpTo("question_screen") { inclusive = true }
                    }
                },
                onMainMenuClick = {
                    //routing back to the main menu
                    navController.popBackStack(Screen.MainMenuScreen.route, inclusive = false)
                }
            )
        }

        //Third screen: Finish screen, receives the score argument from QuestionScreen
        composable(
            route = Screen.FinishScreen.route,
            arguments = listOf(
                //Defining score as argument as an Integer type
                navArgument("score") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            //Extracting the score from the arguments by using backStackEntry
            val score = backStackEntry.arguments?.getInt("score") ?: 0

            FinishScreen(
                score = score,
                onExit = {
                    //Exit button navigates to main menu
                    navController.popBackStack(Screen.MainMenuScreen.route, inclusive = false)
                },
                onRestart = {
                    //Restart button navigates to question screen with index = 0
                    navController.navigate(Screen.QuestionScreen.route) {
                        popUpTo(Screen.MainMenuScreen.route)
                    }
                }
            )
        }
    }
}