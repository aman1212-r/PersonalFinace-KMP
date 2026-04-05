package com.example.personalfinance.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val FinancePurpleStart = Color(0xFFA83BFF)
val FinancePurpleEnd = Color(0xFF5C4DFF)
val FinanceBackground = Color(0xFFF6F7FB)
val FinanceCard = Color(0xFFFFFFFF)
val FinanceSurfaceMuted = Color(0xFFF1F3FA)
val FinanceTextPrimary = Color(0xFF1E2442)
val FinanceTextSecondary = Color(0xFF7D84A3)
val FinanceChartBlue = Color(0xFF4A78FF)
val FinanceChartPink = Color(0xFFFF5F8A)
val FinanceChartOrange = Color(0xFFFFB34A)
val FinanceChartGreen = Color(0xFF3CCB8E)

private val FinanceColors = lightColorScheme(
    primary = FinancePurpleEnd,
    secondary = FinancePurpleStart,
    background = FinanceBackground,
    surface = FinanceCard,
    onBackground = FinanceTextPrimary,
    onSurface = FinanceTextPrimary,
)


private val FinanceShapes = Shapes(
    small = FinanceRadius.small,
    medium = FinanceRadius.medium,
    large = FinanceRadius.large,
)

private val FinanceTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.2).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.1).sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 20.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 15.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 14.sp,
    ),
)

@Composable
fun FinanceTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = FinanceColors,
        typography = FinanceTypography,
        shapes = FinanceShapes,
        content = content,
    )
}
