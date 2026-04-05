package com.example.personalfinance.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalfinance.theme.FinancePurpleEnd
import com.example.personalfinance.theme.FinanceSurfaceMuted
import com.example.personalfinance.theme.FinanceTextSecondary

@Composable
actual fun PlatformSegmentedSelector(
    options: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        options.forEachIndexed { index, label ->
            val selected = selectedIndex == index
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onSelectedIndexChange(index) },
                color = if (selected) FinancePurpleEnd else FinanceSurfaceMuted,
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    color = if (selected) Color.White else FinanceTextSecondary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
