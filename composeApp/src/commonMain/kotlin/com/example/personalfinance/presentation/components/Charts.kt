package com.example.personalfinance.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalfinance.presentation.CategoryBreakdown
import com.example.personalfinance.presentation.formatCurrency
import com.example.personalfinance.theme.FinanceChartBlue
import com.example.personalfinance.theme.FinanceChartGreen
import com.example.personalfinance.theme.FinanceChartOrange
import com.example.personalfinance.theme.FinanceChartPink
import com.example.personalfinance.theme.FinanceTextPrimary
import com.example.personalfinance.theme.FinanceTextSecondary
import kotlin.math.roundToInt

@Composable
fun SpendingPieChart(
    data: List<CategoryBreakdown>,
    modifier: Modifier = Modifier,
) {
    val colors = listOf(FinanceChartBlue, FinanceChartPink, FinanceChartOrange, FinanceChartGreen)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(160.dp)) {
                var startAngle = -90f
                data.forEachIndexed { index, item ->
                    val sweep = item.share * 360f
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = 28f, cap = StrokeCap.Round),
                        size = Size(size.width, size.height),
                    )
                    startAngle += sweep
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Total",
                    color = FinanceTextSecondary,
                )
                Text(
                    text = formatCurrency(data.sumOf { it.amount }),
                    color = FinanceTextPrimary,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            data.take(4).forEachIndexed { index, item ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Canvas(modifier = Modifier.size(10.dp)) {
                        drawCircle(colors[index % colors.size])
                    }
                    Column {
                        Text(
                            text = item.category.title,
                            color = FinanceTextPrimary,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "${(item.share * 100).roundToInt()}% of expenses",
                            color = FinanceTextSecondary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrendLineChart(
    points: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.White,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(top = 8.dp),
    ) {
        if (points.isEmpty()) return@Canvas
        val max = points.maxOrNull() ?: 1f
        val min = points.minOrNull() ?: 0f
        val range = (max - min).takeIf { it > 0f } ?: 1f
        val stepX = size.width / (points.size - 1).coerceAtLeast(1)

        val path = Path()
        points.forEachIndexed { index, point ->
            val x = index * stepX
            val normalized = (point - min) / range
            val y = size.height - (normalized * size.height)
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 6f, cap = StrokeCap.Round),
        )

        points.forEachIndexed { index, point ->
            val x = index * stepX
            val normalized = (point - min) / range
            val y = size.height - (normalized * size.height)
            drawCircle(
                color = lineColor.copy(alpha = 0.24f),
                radius = 10f,
                center = Offset(x, y),
            )
            drawCircle(
                color = lineColor,
                radius = 5f,
                center = Offset(x, y),
            )
        }
    }
}

