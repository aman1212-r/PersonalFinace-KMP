package com.example.personalfinance.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UISegmentedControl
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
actual fun PlatformSegmentedSelector(
    options: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier,
) {
    val callback = remember(onSelectedIndexChange) { SegmentedControlCallback(onSelectedIndexChange) }

    UIKitView(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp),
        factory = {
            UISegmentedControl().apply {
                addTarget(
                    target = callback,
                    action = NSSelectorFromString("onValueChanged:"),
                    forControlEvents = UIControlEventValueChanged,
                )
            }
        },
        update = { control ->
            while (control.numberOfSegments.toInt() > options.size) {
                control.removeSegmentAtIndex(control.numberOfSegments - 1u, animated = false)
            }
            options.forEachIndexed { index, title ->
                if (index < control.numberOfSegments.toInt()) {
                    control.setTitle(title, forSegmentAtIndex = index.toULong())
                } else {
                    control.insertSegmentWithTitle(title, atIndex = index.toULong(), animated = false)
                }
            }
            control.selectedSegmentIndex = selectedIndex.toLong()
        },
    )
}

@OptIn(BetaInteropApi::class)
private class SegmentedControlCallback(
    private val onSelectedIndexChange: (Int) -> Unit,
) : NSObject() {
    @ObjCAction
    fun onValueChanged(sender: UISegmentedControl) {
        onSelectedIndexChange(sender.selectedSegmentIndex.toInt())
    }
}
