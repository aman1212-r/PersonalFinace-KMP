package com.example.personalfinance.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.example.personalfinance.presentation.TransactionFilter

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
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
    PlatformSegmentedSelector(
        options = filters.map { it.title },
        selectedIndex = filters.indexOf(selectedFilter),
        onSelectedIndexChange = { onFilterSelected(filters[it]) },
    )
}
