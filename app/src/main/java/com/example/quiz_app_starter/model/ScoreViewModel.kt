package com.example.quiz_app_starter.model

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * ViewModel to manage the user's quiz scores, including the current best score.
 *
 * Uses Jetpack Compose's state management to automatically update UI components observing
 * this data when it changes.
 */
class ScoreViewModel : ViewModel() {
    //Saving the state of the highest score so far
    var bestScore by mutableStateOf(0)
        private set //making setter private to control value change through functions

    fun updateScore(newScore: Int) {
        //Updating the score if it's higher than the current best score
        if (newScore > bestScore) {
            bestScore = newScore
        }
    }
}