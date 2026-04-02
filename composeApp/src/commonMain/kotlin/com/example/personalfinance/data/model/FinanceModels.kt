package com.example.personalfinance.data.model

enum class TransactionType {
    Income,
    Expense,
}

enum class FinanceCategory(val title: String) {
    Salary("Salary"),
    Food("Food"),
    Transport("Transport"),
    Health("Health"),
    Shopping("Shopping"),
    Education("Education"),
    Bills("Bills"),
    Savings("Savings"),
}

data class Transaction(
    val id: String,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val category: FinanceCategory,
    val date: String,
    val note: String,
)

data class SavingsGoal(
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val streakDays: Int,
)

data class TransactionDraft(
    val id: String? = null,
    val title: String,
    val amount: String,
    val type: TransactionType,
    val category: FinanceCategory,
    val date: String,
    val note: String,
)
