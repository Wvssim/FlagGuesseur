package com.example.quiz_app_wvssim.data.local

import androidx.room.TypeConverter
import com.example.quiz_app_wvssim.domain.model.QuizCategory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QuizTypeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromIntList(value: List<Int>): String = gson.toJson(value)

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromCategory(category: QuizCategory): String = category.name

    @TypeConverter
    fun toCategory(raw: String): QuizCategory = QuizCategory.fromRaw(raw)
}
