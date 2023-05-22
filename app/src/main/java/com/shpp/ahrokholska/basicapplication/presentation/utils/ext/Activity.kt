package com.shpp.ahrokholska.basicapplication.presentation.utils.ext

import android.app.Activity
import android.graphics.Rect
import androidx.window.layout.WindowMetricsCalculator

fun Activity.getBounds(): Rect {
    return WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this).bounds
}

// TODO Remove
//
//fun Activity.getWidth(): Int {
//    return getBounds().width()
//}
//
//fun Activity.getHeight(): Int {
//    return getBounds().height()
//}