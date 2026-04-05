package com.example.personalfinance.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object FinanceSpacing {
    val xSmall = 6.dp
    val small = 10.dp
    val medium = 16.dp
    val large = 20.dp
    val xLarge = 24.dp
}

object FinanceRadius {
    val small = RoundedCornerShape(14.dp)
    val medium = RoundedCornerShape(18.dp)
    val large = RoundedCornerShape(22.dp)
    val xLarge = RoundedCornerShape(28.dp)
    val pill = RoundedCornerShape(999.dp)
}

@Composable
fun financeFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedBorderColor = FinancePurpleEnd.copy(alpha = 0.35f),
    unfocusedBorderColor = Color(0xFFDCE2F2),
    focusedLabelColor = FinancePurpleEnd,
    unfocusedLabelColor = FinanceTextSecondary,
    cursorColor = FinancePurpleEnd,
    focusedTextColor = FinanceTextPrimary,
    unfocusedTextColor = FinanceTextPrimary,
    focusedPlaceholderColor = FinanceTextSecondary,
    unfocusedPlaceholderColor = FinanceTextSecondary,
    disabledContainerColor = Color.White,
    disabledBorderColor = Color(0xFFDCE2F2),
    disabledTextColor = FinanceTextPrimary,
    disabledLabelColor = FinanceTextSecondary,
    disabledTrailingIconColor = FinanceTextSecondary,
    disabledPlaceholderColor = FinanceTextSecondary,
)
