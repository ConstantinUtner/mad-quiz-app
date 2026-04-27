package com.example.quiz_app_starter.data.remote

import com.google.gson.annotations.SerializedName

data class QuestionDto(
    @SerializedName("id") val id: String,
    @SerializedName("category") val category: String,
    @SerializedName("correctAnswer") val correctAnswer: String,
    @SerializedName("incorrectAnswers") val incorrectAnswers: List<String>,
    @SerializedName("question") val question: String,        // ← String
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("type") val type: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("regions") val regions: List<String>?,
    @SerializedName("isNiche") val isNiche: Boolean = false
)

/*
@SerializedName ist der Name im empfangenen JSON-File.
Wir können diesen Namen anders für unseren Code benennen, mit val variable: Datentyp.

z.B.
@SerializedName("correct_answer") val correctAnswer: String")

Im JSON-Objekt heißt der key "correct_answer" und wir bennennen ihn für unseren Code "correctAnswer"

 */