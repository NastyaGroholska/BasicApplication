package com.shpp.ahrokholska.basicapplication.ui.shared

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.apply {
            bottom = space
            if (parent.getChildAdapterPosition(view) == 0) {
                top = space
            }
        }
    }
}