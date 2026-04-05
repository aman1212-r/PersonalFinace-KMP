package com.example.personalfinance.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.personalfinance.presentation.TransactionFilter
import com.example.personalfinance.theme.FinanceRadius
import com.example.personalfinance.theme.financeFieldColors

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        shape = FinanceRadius.medium,
        colors = financeFieldColors(),
        placeholder = { Text("Search transactions") },
        singleLine = true,
    )
}

@Composable
fun FilterChips(
    selectedFilter: TransactionFilter,
    onFilterSelected: (TransactionFilter) -> Unit,
) {
    val filters = TransactionFilter.entries
    FinanceSegmentedControl(
        options = filters.map { it.title },
        selectedIndex = filters.indexOf(selectedFilter),
        onSelectedIndexChange = { onFilterSelected(filters[it]) },
    )
}
