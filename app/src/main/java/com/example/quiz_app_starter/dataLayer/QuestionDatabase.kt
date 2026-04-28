package com.example.quiz_app_starter.dataLayer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [QuestionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class QuestionDatabase : RoomDatabase(){
    abstract val questionDao : QuestionDao //DB knows now about DAO

    companion object{
        @Volatile //never cache the value of instance
        private var instance: QuestionDatabase? = null

        fun getDatabase(context: Context): QuestionDatabase {
            return instance
                ?: synchronized(this) { // wrap in synchronized block to prevent race conditions
                    Room.databaseBuilder(context, QuestionDatabase::class.java, "question_db")
                        .fallbackToDestructiveMigration() // if schema changes wipe the whole db - there are better migration strategies for production usage
                        .build() // create an instance of the db
                        .also {
                            instance = it // override the instance with newly created db
                        }
                }
        }
    }
}