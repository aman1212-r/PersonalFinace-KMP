package com.example.personalfinance.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDestructive
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformDeleteConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val currentOnConfirm = rememberUpdatedState(onConfirm)
    val currentOnDismiss = rememberUpdatedState(onDismiss)

    DisposableEffect(title, message, confirmText, dismissText) {
        val presenter = topViewController() ?: run {
            currentOnDismiss.value()
            return@DisposableEffect onDispose { }
        }

        val alert = UIAlertController.alertControllerWithTitle(
            title = title,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert,
        )
        alert.addAction(
            UIAlertAction.actionWithTitle(
                title = dismissText,
                style = UIAlertActionStyleCancel,
                handler = { currentOnDismiss.value() },
            )
        )
        alert.addAction(
            UIAlertAction.actionWithTitle(
                title = confirmText,
                style = UIAlertActionStyleDestructive,
                handler = { currentOnConfirm.value() },
            )
        )
        presenter.presentViewController(alert, animated = true, completion = null)

        onDispose {
            if (alert.presentingViewController != null) {
                alert.dismissViewControllerAnimated(false, completion = null)
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun topViewController(): UIViewController? {
    val root = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return null
    var controller = root
    while (controller.presentedViewController != null) {
        controller = controller.presentedViewController!!
    }
    return controller
}
