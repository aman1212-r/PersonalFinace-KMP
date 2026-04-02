package com.example.personalfinance.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalfinance.data.model.Transaction
import com.example.personalfinance.presentation.FinanceUiState
import com.example.personalfinance.presentation.TransactionFilter
import com.example.personalfinance.presentation.components.EmptyStateCard
import com.example.personalfinance.presentation.components.FilterChips
import com.example.personalfinance.presentation.components.SearchField
import com.example.personalfinance.presentation.components.SectionTitle
import com.example.personalfinance.presentation.components.TransactionRow
import com.example.personalfinance.theme.FinanceBackground
import com.example.personalfinance.theme.FinanceCard
import com.example.personalfinance.theme.FinanceTextPrimary
import com.example.personalfinance.theme.FinanceTextSecondary

@Composable
fun TransactionsScreen(
    state: FinanceUiState,
    modifier: Modifier = Modifier,
    onSearchChange: (String) -> Unit,
    onFilterSelected: (TransactionFilter) -> Unit,
    onEditTransaction: (Transaction) -> Unit,
    onDeleteTransaction: (Transaction) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FinanceBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SectionTitle("Transactions", "Manage entries")
                SearchField(query = state.searchQuery, onQueryChange = onSearchChange)
                FilterChips(selectedFilter = state.selectedFilter, onFilterSelected = onFilterSelected)
                Text(
                    text = "Tap edit to update an entry or use + to add a new one.",
                    color = FinanceTextSecondary,
                )
            }
        }

        if (state.filteredTransactions.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "No matching transactions",
                    description = "Try a different search term or add a new transaction.",
                )
            }
        } else {
            items(state.filteredTransactions, key = { it.id }) { transaction ->
                TransactionRow(
                    transaction = transaction,
                    onEdit = onEditTransaction,
                    onDelete = onDeleteTransaction,
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = FinanceCard),
            ) {
                Text(
                    text = "Quick add is available from the floating button.",
                    modifier = Modifier.padding(18.dp),
                    color = FinanceTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
