package com.example.personalfinance.data.repository

import com.example.personalfinance.data.model.FinanceCategory
import com.example.personalfinance.data.model.SavingsGoal
import com.example.personalfinance.data.model.Transaction
import com.example.personalfinance.data.model.TransactionDraft
import com.example.personalfinance.data.model.TransactionType
import com.example.personalfinance.presentation.parseAmountOrNull

class InMemoryFinanceRepository : FinanceRepository {
    private val transactions = mutableListOf(
        Transaction("1", "Salary credited", 5200.0, TransactionType.Income, FinanceCategory.Salary, "2026-04-01", "April salary"),
        Transaction("2", "Groceries", 126.5, TransactionType.Expense, FinanceCategory.Food, "2026-04-02", "Weekly grocery refill"),
        Transaction("3", "Metro card", 42.0, TransactionType.Expense, FinanceCategory.Transport, "2026-04-02", "Office commute"),
        Transaction("4", "Streaming bill", 24.0, TransactionType.Expense, FinanceCategory.Bills, "2026-04-03", "Monthly subscription"),
        Transaction("5", "Doctor visit", 88.0, TransactionType.Expense, FinanceCategory.Health, "2026-04-04", "Routine checkup"),
        Transaction("6", "Course refund", 210.0, TransactionType.Income, FinanceCategory.Education, "2026-04-05", "Refund received"),
        Transaction("7", "Savings deposit", 350.0, TransactionType.Expense, FinanceCategory.Savings, "2026-04-05", "Monthly savings transfer"),
        Transaction("8", "New sneakers", 160.0, TransactionType.Expense, FinanceCategory.Shopping, "2026-04-06", "Workout shoes"),
    )

    private var nextId = 9

    override fun getTransactions(): List<Transaction> = transactions.sortedByDescending { it.date }

    override fun getSavingsGoal(): SavingsGoal = SavingsGoal(
        name = "Summer safety fund",
        targetAmount = 3000.0,
        currentAmount = 1840.0,
        streakDays = 12,
    )

    override fun addTransaction(draft: TransactionDraft): Transaction {
        val transaction = draft.toTransaction(id = nextId.toString())
        nextId += 1
        transactions.add(transaction)
        return transaction
    }

    override fun updateTransaction(draft: TransactionDraft): Transaction {
        val id = requireNotNull(draft.id)
        val updated = draft.toTransaction(id)
        val index = transactions.indexOfFirst { it.id == id }
        require(index >= 0)
        transactions[index] = updated
        return updated
    }

    override fun deleteTransaction(id: String) {
        transactions.removeAll { it.id == id }
    }
}

private fun TransactionDraft.toTransaction(id: String): Transaction = Transaction(
    id = id,
    title = title.trim(),
    amount = parseAmountOrNull(amount) ?: 0.0,
    type = type,
    category = category,
    date = date.trim(),
    note = note.trim(),
)
