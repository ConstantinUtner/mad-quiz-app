package com.example.quiz_app_starter.di

import android.content.Context
import com.example.quiz_app_starter.api.QuestionApiService
import com.example.quiz_app_starter.api.TriviaAPI
import com.example.quiz_app_starter.dataLayer.QuestionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuestionAppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): QuestionDatabase {
        return QuestionDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideQuestionDao(db: QuestionDatabase) = db.questionDao

    @Provides
    @Singleton
    fun provideQuestionApiService(): QuestionApiService {
        return TriviaAPI.api
    }
}