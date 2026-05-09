package com.example.personalfinance.presentation.components


import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.coroutines.delay
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIApplication
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeSystem
import platform.UIKit.UIColor
import platform.UIKit.UIControl
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIDatePickerStyle
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UIModalPresentationOverFullScreen
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.darwin.NSObject


@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
actual fun PlatformTransactionDatePicker(
    initialDate: String,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit,
) {
    val formatter = remember {
        NSDateFormatter().apply {
            dateFormat = "yyyy-MM-dd"
        }
    }
    var pickerController by remember { mutableStateOf<UIViewController?>(null) }
    val callback = remember(initialDate, onDismiss, onDateSelected) {
        IOSNativeDatePickerCallback(
            formatter = formatter,
            onDismiss = onDismiss,
            onDateSelected = onDateSelected,
            controllerProvider = { pickerController },
        )
    }

    LaunchedEffect(initialDate) {
        delay(180)
        if (pickerController != null) return@LaunchedEffect

        val presenter = rootViewController() ?: run {
            onDismiss()
            return@LaunchedEffect
        }


        val controller = buildPickerController(
            initialDate = initialDate,
            formatter = formatter,
            callback = callback,
        )
        pickerController = controller
        presenter.presentViewController(controller, animated = true, completion = null)
    }

    DisposableEffect(Unit) {
        onDispose {
            pickerController?.dismissViewControllerAnimated(false, completion = null)
            pickerController = null
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun buildPickerController(
    initialDate: String,
    formatter: NSDateFormatter,
    callback: IOSNativeDatePickerCallback,
): UIViewController {
    val controller = UIViewController().apply {
        modalPresentationStyle = UIModalPresentationOverFullScreen
        view.backgroundColor = UIColor.clearColor
    }


    val dimView = UIControl().apply {
        backgroundColor = UIColor.blackColor.colorWithAlphaComponent(0.24)
        translatesAutoresizingMaskIntoConstraints = false
        addTarget(callback, action = NSSelectorFromString("onCancel"), forControlEvents = UIControlEventTouchUpInside)
    }


    val sheetView = UIView().apply {
        backgroundColor = UIColor.whiteColor
        translatesAutoresizingMaskIntoConstraints = false
        layer.cornerRadius = 28.0
        layer.masksToBounds = true
    }


    val titleLabel = UILabel().apply {
        text = "Select date"
        font = UIFont.boldSystemFontOfSize(17.0)
        textColor = UIColor.blackColor
        translatesAutoresizingMaskIntoConstraints = false
    }


    val cancelButton = UIButton.buttonWithType(UIButtonTypeSystem).apply {
        setTitle("Cancel", forState = 0u)
        translatesAutoresizingMaskIntoConstraints = false
        addTarget(callback, action = NSSelectorFromString("onCancel"), forControlEvents = UIControlEventTouchUpInside)
    }


    val doneButton = UIButton.buttonWithType(UIButtonTypeSystem).apply {
        setTitle("Done", forState = 0u)
        tintColor = UIColor.blackColor
        translatesAutoresizingMaskIntoConstraints = false
        addTarget(callback, action = NSSelectorFromString("onDone"), forControlEvents = UIControlEventTouchUpInside)
    }


    val datePicker = UIDatePicker().apply {
        datePickerMode = UIDatePickerMode.UIDatePickerModeDate
        preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleWheels
        formatter.dateFromString(initialDate)?.let { setDate(it, animated = false) }
        translatesAutoresizingMaskIntoConstraints = false
    }
    callback.attachPicker(datePicker)

    controller.view.addSubview(dimView)
    controller.view.addSubview(sheetView)
    sheetView.addSubview(cancelButton)
    sheetView.addSubview(doneButton)
    sheetView.addSubview(titleLabel)
    sheetView.addSubview(datePicker)

    listOf(
        dimView.topAnchor.constraintEqualToAnchor(controller.view.topAnchor),
        dimView.leadingAnchor.constraintEqualToAnchor(controller.view.leadingAnchor),
        dimView.trailingAnchor.constraintEqualToAnchor(controller.view.trailingAnchor),
        dimView.bottomAnchor.constraintEqualToAnchor(controller.view.bottomAnchor),
        sheetView.leadingAnchor.constraintEqualToAnchor(controller.view.leadingAnchor),
        sheetView.trailingAnchor.constraintEqualToAnchor(controller.view.trailingAnchor),
        sheetView.bottomAnchor.constraintEqualToAnchor(controller.view.bottomAnchor),
        cancelButton.topAnchor.constraintEqualToAnchor(sheetView.safeAreaLayoutGuide.topAnchor, constant = 12.0),
        cancelButton.leadingAnchor.constraintEqualToAnchor(sheetView.layoutMarginsGuide.leadingAnchor),
        doneButton.centerYAnchor.constraintEqualToAnchor(cancelButton.centerYAnchor),
        doneButton.trailingAnchor.constraintEqualToAnchor(sheetView.layoutMarginsGuide.trailingAnchor),
        titleLabel.centerXAnchor.constraintEqualToAnchor(sheetView.centerXAnchor),
        titleLabel.centerYAnchor.constraintEqualToAnchor(cancelButton.centerYAnchor),
        datePicker.topAnchor.constraintEqualToAnchor(cancelButton.bottomAnchor, constant = 8.0),
        datePicker.leadingAnchor.constraintEqualToAnchor(sheetView.leadingAnchor),
        datePicker.trailingAnchor.constraintEqualToAnchor(sheetView.trailingAnchor),
        datePicker.bottomAnchor.constraintEqualToAnchor(sheetView.safeAreaLayoutGuide.bottomAnchor, constant = -12.0),
        datePicker.heightAnchor.constraintEqualToConstant(216.0),
        sheetView.heightAnchor.constraintEqualToConstant(320.0),
    ).forEach { it.active = true }

    return controller
}

@OptIn(ExperimentalForeignApi::class)
private fun rootViewController(): UIViewController? {
    val root = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return null
    var controller = root
    while (controller.presentedViewController != null) {
        controller = controller.presentedViewController!!
    }
    return controller
}

@OptIn(BetaInteropApi::class)
private class IOSNativeDatePickerCallback(
    private val formatter: NSDateFormatter,
    private val onDismiss: () -> Unit,
    private val onDateSelected: (String) -> Unit,
    private val controllerProvider: () -> UIViewController?,
) : NSObject() {
    private var picker: UIDatePicker? = null

    fun attachPicker(value: UIDatePicker) {
        picker = value
    }

    @ObjCAction
    fun onCancel() {
        controllerProvider()?.dismissViewControllerAnimated(true, completion = null)
        onDismiss()
    }

    @ObjCAction
    fun onDone() {
        val selectedDate = picker?.date?.let(formatter::stringFromDate)
        controllerProvider()?.dismissViewControllerAnimated(true, completion = null)
        if (selectedDate != null) {
            onDateSelected(selectedDate)
        } else {
            onDismiss()
        }
    }
}
