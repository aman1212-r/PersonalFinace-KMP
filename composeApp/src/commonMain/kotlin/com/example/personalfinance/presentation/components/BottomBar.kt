package com.example.personalfinance.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.personalfinance.getPlatform
import com.example.personalfinance.presentation.FinanceTab

@Composable
fun FinanceBottomBar(
    selectedTab: FinanceTab,
    onTabSelected: (FinanceTab) -> Unit,
) {
    val isIos = getPlatform().name.startsWith("iOS", ignoreCase = true)

    NavigationBar {
        FinanceTab.entries.forEach { tab ->
            val iconSet = tab.iconSet(isIos = isIos)
            NavigationBarItem(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (selectedTab == tab) iconSet.selected else iconSet.unselected,
                        contentDescription = tab.title,
                        modifier = Modifier.size(22.dp),
                    )
                },
                label = { Text(tab.title) },
            )
        }
    }
}

private data class BottomBarIconSet(
    val selected: ImageVector,
    val unselected: ImageVector,
)

private fun FinanceTab.iconSet(isIos: Boolean): BottomBarIconSet = when (this) {
    FinanceTab.Home -> BottomBarIconSet(
        selected = Icons.Filled.Home,
        unselected = Icons.Outlined.Home,
    )
    FinanceTab.Transactions -> BottomBarIconSet(
        selected = if (isIos) Icons.AutoMirrored.Outlined.ReceiptLong else Icons.AutoMirrored.Filled.ReceiptLong,
        unselected = Icons.AutoMirrored.Outlined.ReceiptLong,
    )
    FinanceTab.Goals -> BottomBarIconSet(
        selected = Icons.Filled.Flag,
        unselected = Icons.Outlined.Flag,
    )
    FinanceTab.Insights -> BottomBarIconSet(
        selected = Icons.Filled.PieChart,
        unselected = Icons.Outlined.PieChart,
    )
}
