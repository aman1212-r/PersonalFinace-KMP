package com.example.personalfinance.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.personalfinance.presentation.FinanceUiState
import com.example.personalfinance.presentation.components.FinanceHeaderCard
import com.example.personalfinance.presentation.components.SectionTitle
import com.example.personalfinance.presentation.components.SpendingPieChart
import com.example.personalfinance.presentation.components.SummaryMetricCard
import com.example.personalfinance.presentation.components.TransactionRow
import com.example.personalfinance.presentation.components.TrendLineChart
import com.example.personalfinance.presentation.formatCurrency
import com.example.personalfinance.theme.FinanceChartBlue
import com.example.personalfinance.theme.FinanceChartGreen
import com.example.personalfinance.theme.FinanceChartPink

private val HomeScreenContentPadding = PaddingValues(bottom = 28.dp)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    state: FinanceUiState,
    modifier: Modifier = Modifier,
    onOpenTransactions: () -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = HomeScreenContentPadding,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            FinanceHeaderCard(
                title = "Personal Finance",
                subtitle = "A clear snapshot of your month",
                amount = formatCurrency(state.summary.balance),
                trailing = "April",
                extendIntoStatusBar = true,
            ) {
                TrendLineChart(
                    points = listOf(30f, 55f, 44f, 76f, 68f, 88f, 71f),
                    modifier = Modifier.padding(top = 10.dp),
                )
            }
        }

        item {
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                maxItemsInEachRow = 2,
            ) {
                SummaryMetricCard("Income", formatCurrency(state.summary.totalIncome), FinanceChartBlue, Modifier.weight(1f))
                SummaryMetricCard("Expenses", formatCurrency(state.summary.totalExpense), FinanceChartPink, Modifier.weight(1f))
                SummaryMetricCard("Savings goal", "${(state.summary.savingsProgress * 100).toInt()}%", FinanceChartGreen, Modifier.weight(1f))
                SummaryMetricCard("Entries", state.transactions.size.toString(), Color(0xFF9D63FF), Modifier.weight(1f))
            }
        }

        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    SectionTitle("Spending breakdown", "This month")
                    SpendingPieChart(data = state.categoryBreakdown)
                }
            }
        }

        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                SectionTitle("Recent activity", "Latest entries")
            }
        }

        items(state.transactions.take(4), key = { it.id }) { transaction ->
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                TransactionRow(
                    transaction = transaction,
                    onEdit = { onOpenTransactions() },
                    onDelete = {},
                )
            }
        }
    }
}
