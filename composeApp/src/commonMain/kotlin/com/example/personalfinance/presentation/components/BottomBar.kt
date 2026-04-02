package com.example.personalfinance.presentation.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.personalfinance.presentation.FinanceTab

@Composable
fun FinanceBottomBar(
    selectedTab: FinanceTab,
    onTabSelected: (FinanceTab) -> Unit,
) {
    NavigationBar(containerColor = Color.White) {
        FinanceTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                icon = {
                    Text(
                        text = tab.title.first().toString(),
                        fontWeight = FontWeight.Bold,
                    )
                },
                label = { Text(tab.title) },
            )
        }
    }
}
