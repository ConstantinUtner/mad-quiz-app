package com.example.quiz_app_starter.model

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ScoreViewModelTest {

    private lateinit var viewModel: ScoreViewModel

    @Before
    fun setup() {
        viewModel = ScoreViewModel()
    }

    @Test
    fun updateScore_withHigherValue_updatesBestScore() {
        // Positive Test: Verify that a higher score updates the bestScore.
        viewModel.updateScore(10)
        assertEquals(10, viewModel.bestScore)
    }

    @Test
    fun updateScore_withLowerValue_doesNotUpdateBestScore() {
        // Negative Test: Verify that a lower score does not overwrite a higher bestScore.
        viewModel.updateScore(20)
        viewModel.updateScore(15)
        assertEquals(20, viewModel.bestScore)
    }

    @Test
    fun updateScore_withSameValue_keepsBestScore() {
        // Edge Case: Verify that the same score keeps the bestScore.
        viewModel.updateScore(25)
        viewModel.updateScore(25)
        assertEquals(25, viewModel.bestScore)
    }
}
