package com.shpp.ahrokholska.basicapplication.presentation.utils.ext

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.enableHorizontalSwipe(crossinline callback: (T) -> Unit) {
    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, 0) {
        override fun getSwipeDirs(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return if (viewHolder is T) ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT else 0
        }

        override fun onMove(
            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false


        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (viewHolder is T) {
                callback(viewHolder)
            }
        }
    }).attachToRecyclerView(this)
}