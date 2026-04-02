package com.example.personalfinance.presentation

import com.example.personalfinance.data.model.Transaction
import com.example.personalfinance.data.model.TransactionDraft

sealed interface FinanceEvent {
    data class SelectTab(val tab: FinanceTab) : FinanceEvent
    data class UpdateSearchQuery(val query: String) : FinanceEvent
    data class UpdateFilter(val filter: TransactionFilter) : FinanceEvent
    data object StartAddTransaction : FinanceEvent
    data class StartEditTransaction(val transaction: Transaction) : FinanceEvent
    data object DismissTransactionEditor : FinanceEvent
    data class SaveTransaction(val draft: TransactionDraft) : FinanceEvent
    data class DeleteTransaction(val transaction: Transaction) : FinanceEvent
}
