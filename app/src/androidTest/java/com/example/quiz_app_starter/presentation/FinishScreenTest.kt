package com.example.quiz_app_starter.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class FinishScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun finishScreen_displaysScore() {
        // Positive Test: Verify that the score passed to the screen is displayed.
        val testScore = 7
        composeTestRule.setContent {
            FinishScreen(score = testScore)
        }

        composeTestRule.onNodeWithText("7").assertIsDisplayed()
        composeTestRule.onNodeWithText("Score:").assertIsDisplayed()
        composeTestRule.onNodeWithText("Game Over!").assertIsDisplayed()
    }

    @Test
    fun finishScreen_restartButtonClick_triggersCallback() {
        // Positive Test: Verify that clicking the restart button triggers the callback.
        var restartClicked = false
        composeTestRule.setContent {
            FinishScreen(onRestart = { restartClicked = true })
        }

        composeTestRule.onNodeWithContentDescription("Restart").performClick()
        assert(restartClicked)
    }

    @Test
    fun finishScreen_homeButtonClick_triggersCallback() {
        // Positive Test: Verify that clicking the home button triggers the callback.
        var homeClicked = false
        composeTestRule.setContent {
            FinishScreen(onExit = { homeClicked = true })
        }

        composeTestRule.onNodeWithContentDescription("Home").performClick()
        assert(homeClicked)
    }
}
