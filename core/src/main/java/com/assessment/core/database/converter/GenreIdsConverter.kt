package com.assessment.core.database.converter

import androidx.room.TypeConverter

class GenreIdsConverter {

    @TypeConverter
    fun fromList(ids: List<Int>): String =
        ids.joinToString(separator = ",", prefix = ",", postfix = ",")

    @TypeConverter
    fun toList(value: String): List<Int> =
        value.trim(',')
            .takeIf { it.isNotBlank() }
            ?.split(",")
            ?.mapNotNull { it.toIntOrNull() }
            ?: emptyList()
}
