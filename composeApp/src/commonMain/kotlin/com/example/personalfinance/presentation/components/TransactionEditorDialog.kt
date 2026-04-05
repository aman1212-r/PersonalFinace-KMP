package com.example.personalfinance.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.personalfinance.data.model.FinanceCategory
import com.example.personalfinance.data.model.Transaction
import com.example.personalfinance.data.model.TransactionDraft
import com.example.personalfinance.data.model.TransactionType
import com.example.personalfinance.theme.FinanceChartPink
import com.example.personalfinance.theme.FinanceRadius
import com.example.personalfinance.theme.FinanceSurfaceMuted
import com.example.personalfinance.theme.FinanceTextSecondary
import com.example.personalfinance.theme.financeFieldColors

@Composable
fun TransactionEditorDialog(
    initialTransaction: Transaction?,
    onDismiss: () -> Unit,
    onSave: (TransactionDraft) -> Unit,
    visible: Boolean,
    errorMessage: String? = null,
) {
    if (!visible) return

    var title by remember(initialTransaction) { mutableStateOf(initialTransaction?.title.orEmpty()) }
    var amount by remember(initialTransaction) { mutableStateOf(initialTransaction?.amount?.toString().orEmpty()) }
    var date by remember(initialTransaction) { mutableStateOf(initialTransaction?.date ?: " ") }
    var note by remember(initialTransaction) { mutableStateOf(initialTransaction?.note.orEmpty()) }
    var type by remember(initialTransaction) { mutableStateOf(initialTransaction?.type ?: TransactionType.Expense) }
    var category by remember(initialTransaction) { mutableStateOf(initialTransaction?.category ?: FinanceCategory.Food) }
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        PlatformTransactionDatePicker(
            initialDate = date,
            onDismiss = { showDatePicker = false },
            onDateSelected = {
                date = it
                showDatePicker = false
            },
        )
        return
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        TransactionDraft(
                            id = initialTransaction?.id,
                            title = title,
                            amount = amount,
                            type = type,
                            category = category,
                            date = date,
                            note = note,
                        )
                    )
                }
            ) {
                Text(if (initialTransaction == null) "Add" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text(if (initialTransaction == null) "Add transaction" else "Edit transaction") },
        text = {
            Column(
                modifier = Modifier.wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    shape = FinanceRadius.medium,
                    colors = financeFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    singleLine = true,
                    shape = FinanceRadius.medium,
                    colors = financeFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                ) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = {},
                        label = { Text("Date") },
                        singleLine = true,
                        readOnly = true,
                        enabled = false,
                        shape = FinanceRadius.medium,
                        colors = financeFieldColors(),
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = "Pick date",
                                tint = FinanceTextSecondary,
                            )
                        },
                    )
                }

                Text("Type", color = FinanceTextSecondary)
                FinanceSegmentedControl(
                    options = listOf("Expense", "Income"),
                    selectedIndex = if (type == TransactionType.Expense) 0 else 1,
                    onSelectedIndexChange = { index ->
                        type = if (index == 0) TransactionType.Expense else TransactionType.Income
                    },
                )

                Text("Category", color = FinanceTextSecondary)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    FinanceCategory.entries.forEach { item ->
                        Surface(
                            modifier = Modifier.clickable { category = item },
                            shape = RoundedCornerShape(16.dp),
                            color = if (category == item) categoryColor(item).copy(alpha = 0.16f) else FinanceSurfaceMuted,
                        ) {
                            Text(
                                text = item.title,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                color = if (category == item) categoryColor(item) else FinanceTextSecondary,
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Notes") },
                    shape = FinanceRadius.medium,
                    colors = financeFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                )

                errorMessage?.let {
                    Text(
                        text = it,
                        color = FinanceChartPink,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        },
    )
}
