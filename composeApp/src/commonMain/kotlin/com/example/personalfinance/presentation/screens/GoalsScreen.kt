package com.example.personalfinance.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.personalfinance.presentation.FinanceUiState
import com.example.personalfinance.presentation.components.BudgetProgressCard
import com.example.personalfinance.presentation.components.FinanceHeaderCard
import com.example.personalfinance.presentation.components.SectionTitle
import com.example.personalfinance.presentation.components.TrendLineChart
import com.example.personalfinance.presentation.formatCurrency
import com.example.personalfinance.theme.FinanceTextPrimary
import com.example.personalfinance.theme.FinanceTextSecondary

private val GoalsScreenContentPadding = PaddingValues(bottom = 28.dp)

@Composable
fun GoalsScreen(
    state: FinanceUiState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = GoalsScreenContentPadding,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            FinanceHeaderCard(
                title = "Savings goal",
                subtitle = state.goal.name,
                amount = formatCurrency(state.goal.currentAmount),
                trailing = "${state.goal.streakDays} day streak",
            ) {
                TrendLineChart(
                    points = listOf(18f, 22f, 25f, 30f, 38f, 42f, 46f),
                    modifier = Modifier.padding(top = 10.dp),
                )
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
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    SectionTitle("Goal progress", "Monthly challenge")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFF1EEFF)),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(state.summary.savingsProgress)
                                .height(14.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF9D63FF)),
                        )
                    }
                    Text(
                        text = "${formatCurrency(state.goal.currentAmount)} saved out of ${formatCurrency(state.goal.targetAmount)}",
                        color = FinanceTextSecondary,
                    )
                    Text(
                        text = "Keep the streak alive by moving a small amount into savings every day.",
                        color = FinanceTextPrimary,
                    )
                }
            }
        }

        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                SectionTitle("Monthly budget", "At a glance")
            }
        }

        items(state.budgetStatuses, key = { it.category.title }) { status ->
            BudgetProgressCard(
                status = status,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}
