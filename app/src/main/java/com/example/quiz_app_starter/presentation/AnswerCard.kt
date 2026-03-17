package com.example.quiz_app_starter.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnswerCard (
    answer: String,
    isSelected: Boolean,
    onSelect: () -> Unit)
{
    Card(
        //Die ganze Karte ist klickbar und handlet den State
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = answer)

            //Radio button selects the current answer (for stated selected option)
            //and deselects if another one is selected
            RadioButton(
                selected = isSelected,
                onClick = null
            )
        }
    }
}