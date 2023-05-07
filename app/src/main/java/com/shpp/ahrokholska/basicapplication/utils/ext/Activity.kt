package com.shpp.ahrokholska.basicapplication.utils.ext

import android.app.Activity
import android.graphics.Rect
import androidx.window.layout.WindowMetricsCalculator

fun Activity.getBounds(): Rect {
    return WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this).bounds
}

fun Activity.getWidth(): Int {
    return getBounds().width()
}

fun Activity.getHeight(): Int {
    return getBounds().height()
}