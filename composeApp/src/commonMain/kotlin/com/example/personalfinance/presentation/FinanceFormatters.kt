package com.example.personalfinance.presentation

import kotlin.math.round

fun formatCurrency(value: Double): String = "Rs. " + formatDecimal(value, 2)

private fun formatDecimal(value: Double, decimals: Int): String {
    val factor = (1..decimals).fold(1.0) { acc, _ -> acc * 10 }
    val rounded = round(value * factor) / factor
    val raw = rounded.toString()
    val parts = raw.split('.')
    val whole = parts.first()
    val fraction = parts.getOrElse(1) { "" }.padEnd(decimals, '0').take(decimals)
    return if (decimals == 0) whole else "$whole.$fraction"
}
