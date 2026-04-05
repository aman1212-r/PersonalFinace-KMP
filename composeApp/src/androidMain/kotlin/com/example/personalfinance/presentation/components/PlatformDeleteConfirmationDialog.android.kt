package com.example.personalfinance.presentation.components

import android.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun PlatformDeleteConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val currentOnConfirm = rememberUpdatedState(onConfirm)
    val currentOnDismiss = rememberUpdatedState(onDismiss)

    DisposableEffect(title, message, confirmText, dismissText, context) {
        var handled = false
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(confirmText) { dialogInterface, _ ->
                handled = true
                dialogInterface.dismiss()
                currentOnConfirm.value()
            }
            .setNegativeButton(dismissText) { dialogInterface, _ ->
                handled = true
                dialogInterface.dismiss()
                currentOnDismiss.value()
            }
            .setOnCancelListener {
                if (!handled) {
                    handled = true
                    currentOnDismiss.value()
                }
            }
            .create()
        dialog.show()

        onDispose {
            if (dialog.isShowing) dialog.dismiss()
        }
    }
}
