package com.example.quiz_app_starter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [QuestionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    companion object {
        const val DATABASE_NAME = "quiz_db"
    }
}