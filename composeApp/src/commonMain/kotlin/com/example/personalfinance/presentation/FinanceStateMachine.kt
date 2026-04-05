package com.example.personalfinance.presentation

import com.example.personalfinance.data.model.Transaction

sealed interface FinanceViewState {
    data object Loading : FinanceViewState
    data class Ready(val uiState: FinanceUiState) : FinanceViewState
    data class Error(val message: String, val uiState: FinanceUiState) : FinanceViewState
}

data class TransactionEditorState(
    val isVisible: Boolean = false,
    val transaction: Transaction? = null,
)

internal data class FinanceMachineContext(
    val selectedTab: FinanceTab = FinanceTab.Home,
    val searchQuery: String = "",
    val selectedFilter: TransactionFilter = TransactionFilter.All,
    val transactionEditor: TransactionEditorState = TransactionEditorState(),
    val deleteConfirmation: DeleteConfirmationState = DeleteConfirmationState(),
)

internal fun reduceFinanceContext(
    context: FinanceMachineContext,
    event: FinanceEvent,
): FinanceMachineContext = when (event) {
    is FinanceEvent.SelectTab -> context.copy(
        selectedTab = event.tab,
        transactionEditor = if (event.tab == FinanceTab.Transactions) {
            context.transactionEditor
        } else {
            TransactionEditorState()
        },
        deleteConfirmation = if (event.tab == FinanceTab.Transactions) {
            context.deleteConfirmation
        } else {
            DeleteConfirmationState()
        },
    )
    is FinanceEvent.UpdateSearchQuery -> context.copy(searchQuery = event.query)
    is FinanceEvent.UpdateFilter -> context.copy(selectedFilter = event.filter)
    is FinanceEvent.StartAddTransaction -> context.copy(
        selectedTab = FinanceTab.Transactions,
        transactionEditor = TransactionEditorState(isVisible = true),
        deleteConfirmation = DeleteConfirmationState(),
    )
    is FinanceEvent.StartEditTransaction -> context.copy(
        selectedTab = FinanceTab.Transactions,
        transactionEditor = TransactionEditorState(
            isVisible = true,
            transaction = event.transaction,
        ),
        deleteConfirmation = DeleteConfirmationState(),
    )
    is FinanceEvent.DismissTransactionEditor -> context.copy(
        transactionEditor = TransactionEditorState(),
    )
    is FinanceEvent.SaveTransaction -> context.copy(
        transactionEditor = TransactionEditorState(),
    )
    is FinanceEvent.RequestDeleteTransaction -> context.copy(
        selectedTab = FinanceTab.Transactions,
        transactionEditor = TransactionEditorState(),
        deleteConfirmation = DeleteConfirmationState(transaction = event.transaction),
    )
    FinanceEvent.ConfirmDeleteTransaction -> context.copy(
        deleteConfirmation = DeleteConfirmationState(),
    )
    FinanceEvent.DismissDeleteConfirmation -> context.copy(
        deleteConfirmation = DeleteConfirmationState(),
    )
}
