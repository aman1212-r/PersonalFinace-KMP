package com.example.personalfinance.data.repository

import com.example.personalfinance.data.model.SavingsGoal
import com.example.personalfinance.data.model.Transaction
import com.example.personalfinance.data.model.TransactionDraft

interface FinanceRepository {
    fun getTransactions(): List<Transaction>
    fun getSavingsGoal(): SavingsGoal
    fun addTransaction(draft: TransactionDraft): Transaction
    fun updateTransaction(draft: TransactionDraft): Transaction
    fun deleteTransaction(id: String)
}
