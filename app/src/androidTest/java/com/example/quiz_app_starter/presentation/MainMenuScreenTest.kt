package com.example.quiz_app_starter.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class MainMenuScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainMenuScreen_displaysCorrectBestScore() {
        // Positive Test: Verify the score is displayed correctly
        val testScore = 50
        composeTestRule.setContent {
            MainMenuScreen(bestScore = testScore)
        }

        composeTestRule.onNodeWithText("50").assertIsDisplayed()
        composeTestRule.onNodeWithText("Best of all time").assertIsDisplayed()
    }

    @Test
    fun mainMenuScreen_playButtonClick_triggersCallback() {
        // Positive Test: Verify button click works
        var clicked = false
        composeTestRule.setContent {
            MainMenuScreen(bestScore = 0, onPlayClick = { clicked = true })
        }

        composeTestRule.onNodeWithText("Play!").performClick()
        assert(clicked)
    }
}
