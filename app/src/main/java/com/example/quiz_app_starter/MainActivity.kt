package com.example.quiz_app_starter

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.quiz_app_starter.navigation.Navigation
import com.example.quiz_app_starter.ui.theme.QuizappstarterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val TAG = "QuizAppTest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate called. (Activity wird erstellt)")
        enableEdgeToEdge()
        setContent {
            QuizappstarterTheme {
                Navigation()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart called. (Activity wird sichtbar)")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume called. (Activity hat den Fokus des Nutzers)")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause called. (Activity verliert den Fokus)")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop called. (Activity ist nicht mehr sichtbar)")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart called. (Activity wird aus dem Hintergrund zurückgeholt)")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy called. (Activity wird komplett zerstört)")
    }
}