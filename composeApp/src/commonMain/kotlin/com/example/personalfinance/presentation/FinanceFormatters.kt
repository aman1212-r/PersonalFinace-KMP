package com.example.personalfinance.presentation

import kotlin.math.round

fun formatCurrency(value: Double): String {
    val safeValue = if (value.isFinite()) value else 0.0
    return "Rs. " + formatDecimal(safeValue, 2)
}

internal fun parseAmountOrNull(raw: String): Double? {
    val normalized = raw
        .trim()
        .replace("Rs.", "", ignoreCase = true)
        .replace("rs.", "", ignoreCase = true)
        .replace(",", "")
        .replace(" ", "")
        .removePrefix("+")

    if (normalized.isBlank()) return null

    val amount = normalized.toDoubleOrNull() ?: return null
    return amount.takeIf { it.isFinite() && it > 0.0 }
}

private fun formatDecimal(value: Double, decimals: Int): String {
    val factor = (1..decimals).fold(1.0) { acc, _ -> acc * 10 }
    val rounded = round(value * factor) / factor
    val raw = rounded.toString()
    val parts = raw.split('.')
    val whole = parts.firstOrNull().orEmpty().ifBlank { "0" }
    val fraction = parts.getOrElse(1) { "" }.padEnd(decimals, '0').take(decimals)
    return if (decimals == 0) whole else "$whole.$fraction"
}
