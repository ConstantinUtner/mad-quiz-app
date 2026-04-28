package com.example.quiz_app_starter.utilities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quiz_app_starter.model.Question
import com.example.quiz_app_starter.presentation.QuestionScreenViewModel

class QuestionScreenViewModelFactory(
    private val questions: List<Question>
) : ViewModelProvider.Factory {

    /*@Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionScreenViewModel::class.java)) {
            return QuestionScreenViewModel(questions) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }*/
}