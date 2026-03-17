package com.example.quiz_app_starter.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.quiz_app_starter.model.Question
import org.junit.Rule
import org.junit.Test

class QuestionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testQuestions = listOf(
        Question(
            category = "test",
            id = "1",
            correctAnswer = "Right",
            answers = listOf("Right", "Wrong"),
            tags = emptyList(),
            question = "Is this a test?",
            type = "text_choice",
            difficulty = "easy",
            regions = emptyList(),
            isNiche = false
        ),
        Question(
            category = "test",
            id = "2",
            correctAnswer = "Yes",
            answers = listOf("Yes", "No"),
            tags = emptyList(),
            question = "Second question?",
            type = "text_choice",
            difficulty = "easy",
            regions = emptyList(),
            isNiche = false
        )
    )

    @Test
    fun questionScreen_displaysQuestionAndAnswers() {
        // Positive Test: Verify the UI elements for the first question are visible.
        composeTestRule.setContent {
            QuestionScreen(questions = testQuestions)
        }

        composeTestRule.onNodeWithText("Is this a test?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Right").assertIsDisplayed()
        composeTestRule.onNodeWithText("Wrong").assertIsDisplayed()
    }

    @Test
    fun questionScreen_submitButton_initiallyDisabled() {
        // Negative/State Test: Submit button should be disabled until an answer is selected.
        composeTestRule.setContent {
            QuestionScreen(questions = testQuestions)
        }

        composeTestRule.onNodeWithText("Submit").assertIsNotEnabled()
    }

    @Test
    fun questionScreen_selectingAnswer_enablesSubmitButton() {
        // Positive Test: Selecting an answer should enable the submit button.
        composeTestRule.setContent {
            QuestionScreen(questions = testQuestions)
        }

        composeTestRule.onNodeWithText("Right").performClick()
        composeTestRule.onNodeWithText("Submit").assertIsEnabled()
    }

    @Test
    fun questionScreen_correctAnswer_showsCorrectDialog() {
        // Positive Test: Submitting the correct answer shows the "Correct!" dialog.
        composeTestRule.setContent {
            QuestionScreen(questions = testQuestions)
        }

        composeTestRule.onNodeWithText("Right").performClick()
        composeTestRule.onNodeWithText("Submit").performClick()

        composeTestRule.onNodeWithText("Correct!").assertIsDisplayed()
    }

    @Test
    fun questionScreen_wrongAnswer_showsWrongDialog() {
        // Positive Test: Submitting the wrong answer shows the "Wrong!" dialog.
        composeTestRule.setContent {
            QuestionScreen(questions = testQuestions)
        }

        composeTestRule.onNodeWithText("Wrong").performClick()
        composeTestRule.onNodeWithText("Submit").performClick()

        composeTestRule.onNodeWithText("Wrong!").assertIsDisplayed()
        composeTestRule.onNodeWithText("The correct answer was: Right").assertIsDisplayed()
    }

    @Test
    fun questionScreen_logoutButton_triggersCallback() {
        // Positive Test: Clicking the exit/logout button triggers the callback.
        var logoutClicked = false
        composeTestRule.setContent {
            QuestionScreen(questions = testQuestions, onMainMenuClick = { logoutClicked = true })
        }

        composeTestRule.onNodeWithContentDescription("Logout").performClick()
        assert(logoutClicked)
    }
}
