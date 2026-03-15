package com.example.quiz_app_starter.navigation

/**
 * Represents the different screens in the application's navigation graph.
 *
 * Each screen is modeled as a sealed class object with an associated route string.
 * The route strings are used by the navigation component to identify destinations.
 */
sealed class Screen(val route: String) {
    object MainMenuScreen : Screen(route = "main_screen")
    object QuestionScreen : Screen(route = "question_screen")
    object FinishScreen : Screen(route = "finish_screen/{score}") {

        /**
         * Creates a route string for the finish screen with the actual [score].
         *
         * @param score The final score to include in the route.
         * @return The complete route string with the score embedded.
         */
        fun createRoute(score: Int) = "finish_screen/$score"
    }
}