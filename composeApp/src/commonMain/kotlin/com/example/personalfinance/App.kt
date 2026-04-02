package com.example.personalfinance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.personalfinance.data.repository.InMemoryFinanceRepository
import com.example.personalfinance.presentation.FinanceEvent
import com.example.personalfinance.presentation.FinanceTab
import com.example.personalfinance.presentation.FinanceViewModel
import com.example.personalfinance.presentation.FinanceViewState
import com.example.personalfinance.presentation.components.FinanceBottomBar
import com.example.personalfinance.presentation.components.TransactionEditorDialog
import com.example.personalfinance.presentation.screens.GoalsScreen
import com.example.personalfinance.presentation.screens.HomeScreen
import com.example.personalfinance.presentation.screens.InsightsScreen
import com.example.personalfinance.presentation.screens.TransactionsScreen
import com.example.personalfinance.theme.FinanceTheme

@Composable
fun App() {
    FinanceTheme {
        val viewModel = remember { FinanceViewModel(InMemoryFinanceRepository()) }
        val viewState = viewModel.viewState

        when (viewState) {
            FinanceViewState.Loading -> LoadingContent()
            is FinanceViewState.Ready -> FinanceContent(
                state = viewState,
                onEvent = viewModel::onEvent,
            )
            is FinanceViewState.Error -> FinanceContent(
                state = viewState,
                onEvent = viewModel::onEvent,
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Loading your finance overview...",
            modifier = Modifier.padding(24.dp),
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun FinanceContent(
    state: FinanceViewState,
    onEvent: (FinanceEvent) -> Unit,
) {
    val uiState = when (state) {
        is FinanceViewState.Ready -> state.uiState
        is FinanceViewState.Error -> state.uiState
        FinanceViewState.Loading -> return
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            FinanceBottomBar(
                selectedTab = uiState.selectedTab,
                onTabSelected = { onEvent(FinanceEvent.SelectTab(it)) },
            )
        },
        floatingActionButton = {
            if (uiState.selectedTab == FinanceTab.Transactions) {
                FloatingActionButton(
                    onClick = { onEvent(FinanceEvent.StartAddTransaction) },
                    containerColor = Color(0xFF5B6CFF),
                    contentColor = Color.White,
                ) {
                    Text("+")
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
        ) {
            when (uiState.selectedTab) {
                FinanceTab.Home -> HomeScreen(
                    state = uiState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    onOpenTransactions = { onEvent(FinanceEvent.SelectTab(FinanceTab.Transactions)) },
                )
                FinanceTab.Transactions -> TransactionsScreen(
                    state = uiState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    onSearchChange = { onEvent(FinanceEvent.UpdateSearchQuery(it)) },
                    onFilterSelected = { onEvent(FinanceEvent.UpdateFilter(it)) },
                    onEditTransaction = { onEvent(FinanceEvent.StartEditTransaction(it)) },
                    onDeleteTransaction = { onEvent(FinanceEvent.DeleteTransaction(it)) },
                )
                FinanceTab.Goals -> GoalsScreen(
                    state = uiState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                )
                FinanceTab.Insights -> InsightsScreen(
                    state = uiState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                )
            }

            if (state is FinanceViewState.Error) {
                ErrorBanner(
                    message = state.message,
                    onDismiss = { onEvent(FinanceEvent.DismissTransactionEditor) },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 12.dp),
                )
            }
        }
    }

    TransactionEditorDialog(
        initialTransaction = uiState.transactionEditor.transaction,
        onDismiss = { onEvent(FinanceEvent.DismissTransactionEditor) },
        onSave = { onEvent(FinanceEvent.SaveTransaction(it)) },
        visible = uiState.transactionEditor.isVisible,
        errorMessage = if (state is FinanceViewState.Error) state.message else null,
    )
}

@Composable
private fun ErrorBanner(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .background(Color(0xFFFFF1F4)),
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(12.dp),
            color = Color(0xFFB3264B),
        )
        Button(
            onClick = onDismiss,
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp),
        ) {
            Text("Dismiss")
        }
    }
}
