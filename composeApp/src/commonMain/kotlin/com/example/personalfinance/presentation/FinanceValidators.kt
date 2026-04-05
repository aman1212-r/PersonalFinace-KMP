package com.example.personalfinance.presentation

import com.example.personalfinance.data.model.Transaction
import com.example.personalfinance.data.model.TransactionDraft

internal fun TransactionDraft.isValidDraft(): Boolean {
    if (title.isBlank() || date.isBlank()) return false
    return parseAmountOrNull(amount) != null
}

internal fun matchesTransactionSearch(
    transaction: Transaction,
    query: String,
): Boolean {
    if (query.isBlank()) return true
    val normalized = query.trim().lowercase()
    return listOf(
        transaction.title,
        transaction.category.title,
        transaction.note,
        transaction.date,
    ).any { it.lowercase().contains(normalized) }
}
