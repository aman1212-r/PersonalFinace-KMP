package com.example.personalfinance.presentation.components

import androidx.compose.runtime.Composable

@Composable
expect fun PlatformTransactionDatePicker(
    initialDate: String,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit,
)

internal fun parseIsoDateToEpochMillis(value: String): Long {
    val parts = value.split("-")
    if (parts.size != 3) return currentFallbackDateMillis()
    val year = parts.getOrNull(0)?.toIntOrNull() ?: return currentFallbackDateMillis()
    val month = parts.getOrNull(1)?.toIntOrNull() ?: return currentFallbackDateMillis()
    val day = parts.getOrNull(2)?.toIntOrNull() ?: return currentFallbackDateMillis()
    return civilDateToEpochDay(year, month, day) * MILLIS_PER_DAY
}

internal fun epochMillisToIsoDate(value: Long): String {
    val epochDay = kotlin.math.floor(value.toDouble() / MILLIS_PER_DAY.toDouble()).toLong()
    val (year, month, day) = epochDayToCivilDate(epochDay)
    return buildString {
        append(year.toString().padStart(4, '0'))
        append('-')
        append(month.toString().padStart(2, '0'))
        append('-')
        append(day.toString().padStart(2, '0'))
    }
}

private fun currentFallbackDateMillis(): Long = civilDateToEpochDay(2026, 4, 7) * MILLIS_PER_DAY

private fun civilDateToEpochDay(year: Int, month: Int, day: Int): Long {
    var adjustedYear = year.toLong()
    val adjustedMonth = month.toLong()
    adjustedYear -= if (adjustedMonth <= 2L) 1 else 0
    val era = if (adjustedYear >= 0) adjustedYear / 400 else (adjustedYear - 399) / 400
    val yearOfEra = adjustedYear - era * 400
    val monthIndex = adjustedMonth + if (adjustedMonth > 2) -3 else 9
    val dayOfYear = (153 * monthIndex + 2) / 5 + day - 1
    val dayOfEra = yearOfEra * 365 + yearOfEra / 4 - yearOfEra / 100 + dayOfYear
    return era * 146097 + dayOfEra - 719468
}

private fun epochDayToCivilDate(epochDay: Long): Triple<Int, Int, Int> {
    val shifted = epochDay + 719468
    val era = if (shifted >= 0) shifted / 146097 else (shifted - 146096) / 146097
    val dayOfEra = shifted - era * 146097
    val yearOfEra = (dayOfEra - dayOfEra / 1460 + dayOfEra / 36524 - dayOfEra / 146096) / 365
    var year = yearOfEra + era * 400
    val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
    val monthPrime = (5 * dayOfYear + 2) / 153
    val day = dayOfYear - (153 * monthPrime + 2) / 5 + 1
    val month = monthPrime + if (monthPrime < 10) 3 else -9
    year += if (month <= 2) 1 else 0
    return Triple(year.toInt(), month.toInt(), day.toInt())
}

private const val MILLIS_PER_DAY = 86_400_000L
