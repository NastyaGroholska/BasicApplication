package com.shpp.ahrokholska.basicapplication.ui.shared

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import com.shpp.ahrokholska.basicapplication.R


class CustomButton(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatButton(context, attrs) {
    var icon: Drawable? = null
        set(value) {
            field = value
            invalidate()
        }
    var iconHeight: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var iconPadding: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var cornerRadius: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var isTextAllCaps: Boolean = true
        set(value) {
            field = value
            invalidate()
        }
    private val drawPaint = Paint()
    private val textBounds = Rect()

    init {
        with(
            context.theme.obtainStyledAttributes(
                attrs, R.styleable.CustomButton, 0, 0
            )
        ) {
            try {
                cornerRadius =
                    getDimension(R.styleable.CustomButton_cornerRadius, DEFAULT_CORNER_RADIUS)
                icon = getDrawable(R.styleable.CustomButton_icon)
                iconHeight = getDimension(
                    R.styleable.CustomButton_iconHeight,
                    icon?.intrinsicHeight?.toFloat() ?: 0f
                )
                iconPadding = getDimension(R.styleable.CustomButton_iconPadding, 0f)
            } finally {
                recycle()
            }
        }

        with(
            context.theme.obtainStyledAttributes(
                attrs, intArrayOf(
                    android.R.attr.background, android.R.attr.text, android.R.attr.textAllCaps
                ), 0, 0
            )
        ) {
            try {
                background = getDrawable(0)
                @SuppressLint("ResourceType")
                text = getText(1) ?: DEFAULT_TEXT
                @SuppressLint("ResourceType")
                isTextAllCaps = getBoolean(2, true)
            } finally {
                recycle()
            }
        }
    }

    override fun setBackground(background: Drawable?) {
        if (background is ColorDrawable) {
            with(
                ShapeDrawable(
                    RoundRectShape(
                        Array(8) { cornerRadius }.toFloatArray(), null, null
                    )
                )
            ) {
                paint.color = background.color
                super.setBackground(this)
            }
        } else {
            super.setBackground(background)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return

        val textString = text.toString()
        val textStringCase = if (isTextAllCaps) textString.uppercase() else textString

        with(drawPaint) {
            textSize = this@CustomButton.textSize
            typeface = this@CustomButton.typeface
            color = currentTextColor
            style = Paint.Style.FILL
            getTextBounds(textStringCase, 0, textStringCase.length, textBounds)
        }

        val textWidth = textBounds.width()
        val iconWidth = drawIconCentered(canvas, textWidth)

        canvas.drawText(
            textStringCase,
            (measuredWidth - textWidth + iconWidth + iconPadding) / 2f - textBounds.left,
            (measuredHeight + textBounds.height()) / 2f - textBounds.bottom,
            drawPaint
        )
    }

    private fun drawIconCentered(canvas: Canvas, textWidth: Int): Int {
        if (icon == null) return 0

        var tmpIconHeight = icon?.intrinsicWidth ?: 0
        var tmpIconWidth = icon?.intrinsicHeight ?: 0
        if (iconHeight != 0f) {
            val ratio = tmpIconHeight / iconHeight
            tmpIconHeight = (tmpIconHeight / ratio).toInt()
            tmpIconWidth = (tmpIconWidth / ratio).toInt()
        }

        val combinedWidth = textWidth + tmpIconWidth + iconPadding.toInt()

        icon?.setBounds(
            (measuredWidth - combinedWidth) / 2,
            (measuredHeight - tmpIconHeight) / 2,
            (measuredWidth - combinedWidth) / 2 + tmpIconWidth,
            (measuredHeight + tmpIconHeight) / 2
        )
        icon?.draw(canvas)
        return tmpIconWidth
    }

    companion object {
        private const val DEFAULT_TEXT = "CustomButton"
        private const val DEFAULT_CORNER_RADIUS = 10f
    }
}