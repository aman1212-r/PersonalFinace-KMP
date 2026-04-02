package com.example.personalfinance.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.personalfinance.data.model.FinanceCategory
import com.example.personalfinance.data.model.Transaction
import com.example.personalfinance.data.repository.FinanceRepository

class FinanceViewModel(
    private val repository: FinanceRepository,
) : ViewModel() {

    private var machineContext = FinanceMachineContext()

    var viewState by mutableStateOf<FinanceViewState>(FinanceViewState.Loading)
        private set

    init {
        publishReadyState()
    }

    fun onEvent(event: FinanceEvent) {
        val currentUiState = currentUiState()
        runCatching {
            when (event) {
                is FinanceEvent.SaveTransaction -> handleSaveTransaction(event)
                is FinanceEvent.DeleteTransaction -> handleDeleteTransaction(event)
                else -> {
                    machineContext = reduceFinanceContext(machineContext, event)
                    publishReadyState()
                }
            }
        }.onFailure { throwable ->
            viewState = FinanceViewState.Error(
                message = throwable.message ?: "Something went wrong while updating your finance state.",
                uiState = currentUiState,
            )
        }
    }

    private fun handleSaveTransaction(event: FinanceEvent.SaveTransaction) {
        if (!event.draft.isValidDraft()) {
            viewState = FinanceViewState.Error(
                message = "Enter a title, a valid amount, and a date.",
                uiState = currentUiState(),
            )
            return
        }

        if (event.draft.id == null) {
            repository.addTransaction(event.draft)
        } else {
            repository.updateTransaction(event.draft)
        }

        machineContext = reduceFinanceContext(machineContext, event)
        publishReadyState()
    }

    private fun handleDeleteTransaction(event: FinanceEvent.DeleteTransaction) {
        repository.deleteTransaction(event.transaction.id)
        machineContext = reduceFinanceContext(machineContext, event)
        publishReadyState()
    }

    private fun publishReadyState() {
        viewState = FinanceViewState.Ready(buildUiState())
    }

    private fun currentUiState(): FinanceUiState = when (val current = viewState) {
        FinanceViewState.Loading -> buildUiState()
        is FinanceViewState.Ready -> current.uiState
        is FinanceViewState.Error -> current.uiState
    }

    private fun buildUiState(): FinanceUiState {
        val transactions = repository.getTransactions()
        val goal = repository.getSavingsGoal()
        val income = transactions.filter { it.type == com.example.personalfinance.data.model.TransactionType.Income }.sumOf { it.amount }
        val expenses = transactions.filter { it.type == com.example.personalfinance.data.model.TransactionType.Expense }.sumOf { it.amount }
        val balance = income - expenses
        val savingsProgress = (goal.currentAmount / goal.targetAmount).toFloat().coerceIn(0f, 1f)

        val filteredTransactions = transactions.filter { transaction ->
            machineContext.selectedFilter.matches(transaction.type) &&
                matchesTransactionSearch(transaction, machineContext.searchQuery)
        }

        val expenseTransactions = transactions.filter { it.type == com.example.personalfinance.data.model.TransactionType.Expense }
        val totalExpense = expenseTransactions.sumOf { it.amount }.takeIf { it > 0 } ?: 1.0
        val categoryBreakdown = expenseTransactions
            .groupBy { it.category }
            .map { (category, items) ->
                val amount = items.sumOf { it.amount }
                CategoryBreakdown(
                    category = category,
                    amount = amount,
                    share = (amount / totalExpense).toFloat(),
                )
            }
            .sortedByDescending { it.amount }

        val budgetStatuses = listOf(
            buildBudgetStatus(FinanceCategory.Transport, expenseTransactions, 400.0),
            buildBudgetStatus(FinanceCategory.Food, expenseTransactions, 650.0),
            buildBudgetStatus(FinanceCategory.Shopping, expenseTransactions, 500.0),
        )

        val highestCategory = categoryBreakdown.firstOrNull()
        val thisWeek = expenseTransactions.take(4).sumOf { it.amount }
        val previousWeek = expenseTransactions.drop(4).take(4).sumOf { it.amount }
        val comparison = if (previousWeek == 0.0) 0 else (((thisWeek - previousWeek) / previousWeek) * 100).toInt()

        val insights = listOf(
            InsightCard(
                title = "Top category",
                value = highestCategory?.category?.title ?: "No spend",
                description = highestCategory?.let { "${formatCurrency(it.amount)} spent this month" }
                    ?: "Start tracking to see patterns",
            ),
            InsightCard(
                title = "Weekly change",
                value = "${if (comparison > 0) "+" else ""}$comparison%",
                description = "Compared with the previous week",
            ),
            InsightCard(
                title = "Income ratio",
                value = "${((income / (income + expenses).coerceAtLeast(1.0)) * 100).toInt()}%",
                description = "Share of incoming cash flow",
            ),
        )

        return FinanceUiState(
            summary = FinanceSummary(
                balance = balance,
                totalIncome = income,
                totalExpense = expenses,
                savingsProgress = savingsProgress,
            ),
            goal = goal,
            transactions = transactions,
            filteredTransactions = filteredTransactions,
            categoryBreakdown = categoryBreakdown,
            budgetStatuses = budgetStatuses,
            insights = insights,
            searchQuery = machineContext.searchQuery,
            selectedFilter = machineContext.selectedFilter,
            selectedTab = machineContext.selectedTab,
            transactionEditor = machineContext.transactionEditor,
        )
    }

    private fun buildBudgetStatus(
        category: FinanceCategory,
        expenses: List<Transaction>,
        limit: Double,
    ): BudgetStatus {
        val spent = expenses.filter { it.category == category }.sumOf { it.amount }
        val isHealthy = spent <= limit * 0.8
        return BudgetStatus(
            category = category,
            spent = spent,
            limit = limit,
            message = if (isHealthy) "Spending is on track" else "Close to your budget limit",
        )
    }
}
