package com.example.personalfinance.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.personalfinance.data.model.FinanceCategory
import com.example.personalfinance.data.model.Transaction
import com.example.personalfinance.data.model.TransactionType
import com.example.personalfinance.presentation.formatCurrency
import com.example.personalfinance.theme.FinanceChartBlue
import com.example.personalfinance.theme.FinanceChartGreen
import com.example.personalfinance.theme.FinanceChartOrange
import com.example.personalfinance.theme.FinanceChartPink
import com.example.personalfinance.theme.FinancePurpleEnd
import com.example.personalfinance.theme.FinanceTextPrimary
import com.example.personalfinance.theme.FinanceTextSecondary

@Composable
fun TransactionRow(
    transaction: Transaction,
    onEdit: (Transaction) -> Unit,
    onDelete: (Transaction) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CategoryBadge(category = transaction.category)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                ) {
                    Text(
                        text = transaction.title,
                        color = FinanceTextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "${transaction.category.title} • ${transaction.date}",
                        color = FinanceTextSecondary,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (transaction.note.isNotBlank()) {
                        Text(
                            text = transaction.note,
                            color = FinanceTextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .widthIn(min = 96.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = (if (transaction.type == TransactionType.Income) "+" else "-") + formatCurrency(transaction.amount),
                    color = if (transaction.type == TransactionType.Income) FinanceChartGreen else FinanceTextPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Edit",
                        color = FinancePurpleEnd,
                        modifier = Modifier.clickable { onEdit(transaction) },
                    )
                    Text(
                        text = "Delete",
                        color = FinanceChartPink,
                        modifier = Modifier.clickable { onDelete(transaction) },
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBadge(category: FinanceCategory) {
    val accent = categoryColor(category)
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(accent.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = category.title.take(1),
            color = accent,
            fontWeight = FontWeight.Bold,
        )
    }
}

fun categoryColor(category: FinanceCategory): Color = when (category) {
    FinanceCategory.Salary -> FinanceChartBlue
    FinanceCategory.Food -> FinanceChartPink
    FinanceCategory.Transport -> FinanceChartOrange
    FinanceCategory.Health -> Color(0xFF8D63FF)
    FinanceCategory.Shopping -> Color(0xFF7D67FF)
    FinanceCategory.Education -> Color(0xFF6C8CFF)
    FinanceCategory.Bills -> Color(0xFF5FCF9B)
    FinanceCategory.Savings -> FinanceChartGreen
}
