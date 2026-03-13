package com.example.quiz_app_starter.navigation

sealed class Screen(val route: String) {
    object MainMenuScreen : Screen(route = "main_screen")
    object QuestionScreen : Screen(route = "question_screen")
    object FinishScreen : Screen(route = "finish_screen/{score}") {
        fun createRoute(score: Int) = "finish_screen/$score"
    }
}