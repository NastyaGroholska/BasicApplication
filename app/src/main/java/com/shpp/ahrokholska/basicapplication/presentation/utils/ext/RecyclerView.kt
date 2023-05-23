package com.shpp.ahrokholska.basicapplication.presentation.utils.ext

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.enableHorizontalSwipe(callback: (RecyclerView.ViewHolder) -> Unit) {
    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false


        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            callback(viewHolder)
        }
    }).attachToRecyclerView(this)
}