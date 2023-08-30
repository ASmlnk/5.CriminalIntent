package com.bignerdranch.android.a5criminalintent

import android.content.Context
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeHelper(private val recyclerView: RecyclerView) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.ACTION_STATE_IDLE,
    ItemTouchHelper.LEFT
) {
    private val swipedPosition = -1
    private val buttonsBuffer: MutableMap<Int, List<UnderlayButton>>
}

interface UnderlayButtonClickListener {
    fun onClick()
}

class UnderlayButton(
    private val context: Context,
    private val title: String,
    textSize: Float,
    @ColorRes private val colorRes: Int,
    private val clickListener: UnderlayButtonClickListener
    ) {

}