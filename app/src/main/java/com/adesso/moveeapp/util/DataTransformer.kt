package com.adesso.moveeapp.util

import com.adesso.moveeapp.data.model.genresmodel.Genre
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

object DataTransformer {
    inline fun <T, R> transformData(source: T, transformFunction: (T) -> R): R {
        return transformFunction(source)
    }

    inline fun <T1, T2, R> transformData(
        source1: T1,
        source2: T2,
        transformFunction: (T1, T2) -> R
    ): R {
        return transformFunction(source1, source2)
    }

    fun transformGenre(currentGenreList: List<Int>, allGenreListItem: List<Genre>): String {
        val matchingGenres = allGenreListItem.filter { it.id in currentGenreList }
        return matchingGenres.joinToString(separator = ", ") { it.name }
    }

    fun concatBornStatus(birthDate: String, placeOfBirth: String): String {
        val formattedBornDate = transformDateFormat(birthDate)
        return if (formattedBornDate != "" && placeOfBirth != "")
            "$formattedBornDate, $placeOfBirth."
        else
            ""
    }

    fun roundToNearest(number: Double) : Double {
        return (number * 10).roundToInt() / 10.0
    }

    fun transformSpecialDateFormat(inputDate: String?) : String {
        return if(!inputDate.isNullOrEmpty()) {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val outputDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

            val date: Date = inputDateFormat.parse(inputDate) as Date
            val formattedDate = outputDateFormat.format(date)
            return formattedDate
        } else {
            ""
        }

    }

    private fun transformDateFormat(birthDate: String): String {
        return if (birthDate.isNotBlank()) {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val outputDateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val date = inputDateFormat.parse(birthDate)

            outputDateFormat.format(date!!)
        } else {
            ""
        }
    }
}