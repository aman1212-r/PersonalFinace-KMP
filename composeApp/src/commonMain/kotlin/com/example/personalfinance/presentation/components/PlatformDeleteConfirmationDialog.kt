package com.example.personalfinance.presentation.components

import androidx.compose.runtime.Composable

@Composable
expect fun PlatformDeleteConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
)
