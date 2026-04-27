package com.example.quiz_app_starter.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value ?: emptyList<String>())
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type // Sichere Variante gegen Type Erasures
        return gson.fromJson(value, type) ?: emptyList()
    }
}

/* ---- Bsp aus den Folien ----
import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun listToJson(value: List<String>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String): List<String> = Gson().fromJson(value, Array<String>::class.java).toList()
}
 */