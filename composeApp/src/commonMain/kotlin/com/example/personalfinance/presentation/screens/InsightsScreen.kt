package com.example.personalfinance.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalfinance.presentation.FinanceUiState
import com.example.personalfinance.presentation.InsightCard
import com.example.personalfinance.presentation.components.SectionTitle
import com.example.personalfinance.presentation.components.categoryColor
import com.example.personalfinance.presentation.formatCurrency
import com.example.personalfinance.theme.FinanceBackground
import com.example.personalfinance.theme.FinanceCard
import com.example.personalfinance.theme.FinanceTextPrimary
import com.example.personalfinance.theme.FinanceTextSecondary

private val InsightsScreenContentPadding = PaddingValues(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 28.dp)

@Composable
fun InsightsScreen(
    state: FinanceUiState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FinanceBackground),
        contentPadding = InsightsScreenContentPadding,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                SectionTitle("Insights", "Simple patterns")
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    state.insights.take(2).forEach { insight ->
                        InsightSummaryCard(
                            insight = insight,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                InsightSummaryCard(
                    insight = state.insights.last(),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    SectionTitle("Category bars", "Expense mix")
                    state.categoryBreakdown.forEach { item ->
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(item.category.title, color = FinanceTextPrimary, fontWeight = FontWeight.SemiBold)
                                Text(formatCurrency(item.amount), color = FinanceTextSecondary)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0xFFF1EEFF)),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(item.share)
                                        .height(10.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(categoryColor(item.category)),
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = FinanceCard),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Why this screen matters",
                        color = FinanceTextPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "The screen keeps insights compact and readable on mobile while making patterns visible without heavy analytics.",
                        color = FinanceTextSecondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun InsightSummaryCard(
    insight: InsightCard,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = insight.title,
                color = FinanceTextSecondary,
            )
            Text(
                text = insight.value,
                color = FinanceTextPrimary,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = insight.description,
                color = FinanceTextSecondary,
            )
        }
    }
}
