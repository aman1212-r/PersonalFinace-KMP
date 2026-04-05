package com.example.personalfinance.presentation

import com.example.personalfinance.data.model.FinanceCategory
import com.example.personalfinance.data.model.SavingsGoal
import com.example.personalfinance.data.model.Transaction
import com.example.personalfinance.data.model.TransactionType

enum class FinanceTab(val title: String) {
    Home("Home"),
    Transactions("Transactions"),
    Goals("Goals"),
    Insights("Insights"),
}

enum class TransactionFilter(val title: String) {
    All("All"),
    Expense("Expense"),
    Income("Income"),
}

data class FinanceSummary(
    val balance: Double,
    val totalIncome: Double,
    val totalExpense: Double,
    val savingsProgress: Float,
)

data class CategoryBreakdown(
    val category: FinanceCategory,
    val amount: Double,
    val share: Float,
)

data class BudgetStatus(
    val category: FinanceCategory,
    val spent: Double,
    val limit: Double,
    val message: String,
)

data class InsightCard(
    val title: String,
    val value: String,
    val description: String,
)

data class DeleteConfirmationState(
    val transaction: Transaction? = null,
) {
    val isVisible: Boolean get() = transaction != null
}

data class FinanceUiState(
    val summary: FinanceSummary,
    val goal: SavingsGoal,
    val transactions: List<Transaction>,
    val filteredTransactions: List<Transaction>,
    val categoryBreakdown: List<CategoryBreakdown>,
    val budgetStatuses: List<BudgetStatus>,
    val insights: List<InsightCard>,
    val searchQuery: String,
    val selectedFilter: TransactionFilter,
    val selectedTab: FinanceTab,
    val transactionEditor: TransactionEditorState,
    val deleteConfirmation: DeleteConfirmationState,
)

fun TransactionFilter.matches(type: TransactionType): Boolean = when (this) {
    TransactionFilter.All -> true
    TransactionFilter.Expense -> type == TransactionType.Expense
    TransactionFilter.Income -> type == TransactionType.Income
}
