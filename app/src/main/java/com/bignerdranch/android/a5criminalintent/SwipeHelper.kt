package com.bignerdranch.android.a5criminalintent

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.math.abs
import kotlin.math.max


@SuppressLint("ClickableViewAccessibility")
abstract class SwipeHelper(private val recyclerView: RecyclerView) : ItemTouchHelper.SimpleCallback(
    0,      //ItemTouchHelper.ACTION_STATE_IDLE,
    ItemTouchHelper.LEFT
) {

    private var swipedPosition = -1
    private val buttonsBuffer: MutableMap<Int, List<UnderlayButton>> = mutableMapOf()
    private val recoverQueue = object : LinkedList<Int>() {
        override fun add(element: Int): Boolean {
            if (contains(element)) return false
            return super.add(element)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val touchListener = View.OnTouchListener { _, event ->
        if (swipedPosition < 0) return@OnTouchListener false
        buttonsBuffer[swipedPosition]?.forEach { it.handle(event) }
        recoverQueue.add(swipedPosition)
        Log.i("REC", "$recoverQueue")
        swipedPosition = -1
        recoverSwipedItem()

        true
    }

    init {
        recyclerView.setOnTouchListener(touchListener)
    }

    private fun recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            val position = recoverQueue.poll() ?: return
            //recyclerView.adapter?.notifyItemChanged(position)
        }
    }

    private fun drawButtons(
        canvas: Canvas,
        buttons: List<UnderlayButton>,
        itemView: View,
        dX: Float
    ) {
        var right = itemView.right
        buttons.forEach { button ->
            val width = button.intrinsicWidth / buttons.intrinsicWidth() * abs(dX)
            val left = right - width
            button.draw(
                canvas,
                RectF(left, itemView.top.toFloat(), right.toFloat(), itemView.bottom.toFloat())
            )
            right = left.toInt()
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val position = viewHolder.adapterPosition
        var maxDX = dX
        val itemView = viewHolder.itemView

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                if (!buttonsBuffer.containsKey(position)) {
                    buttonsBuffer[position] = instantiateUnderlayButton(position)
                }
                Log.i("REC1", "$buttonsBuffer")
                val buttons = buttonsBuffer[position] ?: return
                if (buttons.isEmpty()) return
                maxDX = max(-buttons.intrinsicWidth(), dX)

                drawButtons(c, buttons, itemView, maxDX)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, maxDX, dY, actionState, isCurrentlyActive)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (swipedPosition != position) recoverQueue.add(swipedPosition)
        swipedPosition = position
        recoverSwipedItem()
    }

    abstract fun instantiateUnderlayButton(position: Int): List<UnderlayButton>


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
        private var clickableRegion: RectF? = null
        private val textSizeInPixel: Float = textSize * context.resources.displayMetrics.density
        private val horizontalPadding = 50.0f
        val intrinsicWidth: Float

        init {
            val paint = Paint()
            paint.textSize = textSizeInPixel
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.textAlign = Paint.Align.LEFT
            val titleBounds = Rect()
            paint.getTextBounds(title, 0, title.length, titleBounds)
            intrinsicWidth = titleBounds.width() + 2 * horizontalPadding
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun draw2(canvas: Canvas, rect: RectF) {
            val t = context.resources.getDrawable(R.drawable.ic_baseline_delete_24, null)
            val deltaTop = (rect.bottom - rect.top) / 3
            val deltaLeft = (rect.right - rect.left) / 3

            t.bounds = Rect(
                (rect.left + deltaLeft).toInt(),
                (rect.top + deltaTop).toInt(),
                (rect.right - deltaLeft).toInt(),
                (rect.bottom - deltaTop).toInt()
            )
            t.draw(canvas)
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun draw(canvas: Canvas, rect: RectF) {
            val paint = Paint()

            // Draw background
            paint.color = ContextCompat.getColor(context, colorRes)
            canvas.drawRect(rect, paint)

            //Draw title
            paint.color = ContextCompat.getColor(context, android.R.color.holo_red_light)
            paint.textSize = textSizeInPixel
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.textAlign = Paint.Align.LEFT

            val titleBounds = Rect()
            paint.getTextBounds(title, 0, title.length, titleBounds)

            //val y = rect.height() / 2 + titleBounds.height() / 2 - titleBounds.bottom
            val iconDelete = context.resources.getDrawable(R.drawable.ic_baseline_delete_24, null)
            val y =
                rect.height() - (rect.height() - titleBounds.height() - iconDelete.intrinsicHeight) / 2

            canvas.drawText(title, rect.left + horizontalPadding, rect.top + y, paint)
            val yTop = (rect.height() - titleBounds.height() - iconDelete.intrinsicHeight) / 2
            val yBottom = yTop + titleBounds.height()
            val yLeft = (rect.width() - iconDelete.intrinsicWidth) / 2

            iconDelete.bounds = Rect(
                (rect.left + yLeft).toInt(),
                (rect.top + yTop).toInt(),
                (rect.right - yLeft).toInt(),
                (rect.bottom - yBottom).toInt()
            )
            iconDelete.draw(canvas)
            // draw2(canvas, rect)
            clickableRegion = rect
        }

        fun handle(event: MotionEvent) {
            clickableRegion?.let {
                if (it.contains(event.x, event.y)) {
                    clickListener.onClick()
                }
            }
        }
    }
}

private fun List<SwipeHelper.UnderlayButton>.intrinsicWidth(): Float {
    if (isEmpty()) return 0.0f
    return map {
        it.intrinsicWidth
    }.reduce { acc, fl -> acc + fl }
}