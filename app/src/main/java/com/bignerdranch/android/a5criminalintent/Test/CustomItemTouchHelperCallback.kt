package com.bignerdranch.android.a5criminalintent.Test

import android.graphics.Canvas
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

class CustomItemTouchHelperCallback : ItemTouchHelper.Callback {

    companion object {
        private var TAG: String = CustomItemTouchHelperCallback::class.java.simpleName
        private const val ALPHA_FULL = 1.0f
        private const val ALPHA_BIT = 0.7f
        private const val ALPHA_HALF = 0.5f
        private const val ALPHA_DURATION = 200L
        private const val LINEAR_DRAG_FLAGS: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        private const val LINEAR_SWIPE_FLAGS: Int = ItemTouchHelper.START or ItemTouchHelper.END
        private const val GRID_SWIPE_FLAGS: Int = ItemTouchHelper.ACTION_STATE_IDLE
        private const val GRID_DRAG_FLAGS: Int =
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    }

    private var canDrag: Boolean by Delegates.notNull<Boolean>()
    private var canSwipe: Boolean by Delegates.notNull<Boolean>()
    private var withFadeOutSwipe: Boolean by Delegates.notNull<Boolean>()
    private var withTransparentDrag: Boolean by Delegates.notNull<Boolean>()

    private val customItemTouchHelperListener: CustomItemTouchHelperListener

    constructor(
        customItemTouchHelperListener: CustomItemTouchHelperListener,
        canDrag: Boolean,
        canSwipe: Boolean
    ) : super() {
        this.customItemTouchHelperListener = customItemTouchHelperListener
        this.canDrag = canDrag
        this.canSwipe = canSwipe
        withFadeOutSwipe = false
        withTransparentDrag = false
    }

    fun setFadeOutSwipe(withFadeOutSwipe: Boolean) {
        this.withFadeOutSwipe = withFadeOutSwipe
    }

    fun setTransparentDrag(withTransparentDrag: Boolean) {
        this.withTransparentDrag = withTransparentDrag
    }

    override fun isLongPressDragEnabled(): Boolean {
        //return super.isLongPressDragEnabled()
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        //return super.isItemViewSwipeEnabled()
        return canSwipe
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        when  {
            actionState == ItemTouchHelper.ACTION_STATE_IDLE -> {
               Log.d(TAG, "ItemTouchHelper.ACTION_STATE_IDLE")
           }
            actionState == ItemTouchHelper.ACTION_STATE_SWIPE -> {
                Log.d(TAG, "ItemTouchHelper.ACTION_STATE_SWIPE")
            }
            actionState == ItemTouchHelper.ACTION_STATE_DRAG && withTransparentDrag -> {
                Log.d(TAG, "ItemTouchHelper.ACTION_STATE_DRAG")
                viewHolder?.itemView?.animate()?.alpha(ALPHA_BIT)?.duration = ALPHA_DURATION
            }
            actionState == ItemTouchHelper.ACTION_STATE_DRAG && !withTransparentDrag -> {
                Log.d(TAG, "ItemTouchHelper.ACTION_STATE_DRAG")
            }
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when {
            canSwipe -> {
                customItemTouchHelperListener.onItemSwiped(viewHolder.adapterPosition)
            }
            else -> {

            }
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
        when {
            withFadeOutSwipe && actionState === ItemTouchHelper.ACTION_STATE_SWIPE -> {
                val alpha = ALPHA_FULL - Math.abs(dX) / (viewHolder.itemView.width?:0)
                viewHolder.itemView.alpha = alpha
                viewHolder.itemView.translationX = dX
            }
            else -> {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return when {
            canDrag && viewHolder.itemViewType == target.itemViewType -> {
                customItemTouchHelperListener.onItemMove(viewHolder.adapterPosition, target.itemViewType)
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return when {
            recyclerView.layoutManager is GridLayoutManager -> {
                ItemTouchHelper.Callback.makeMovementFlags(GRID_DRAG_FLAGS, GRID_SWIPE_FLAGS)
            }
            recyclerView.layoutManager is LinearLayoutManager -> {
                ItemTouchHelper.Callback.makeMovementFlags(LINEAR_DRAG_FLAGS, LINEAR_SWIPE_FLAGS)
            }
            else -> {
                ItemTouchHelper.Callback.makeMovementFlags(LINEAR_DRAG_FLAGS, LINEAR_SWIPE_FLAGS)
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.alpha = ALPHA_FULL
        customItemTouchHelperListener.onItemClear(viewHolder.adapterPosition)
    }
}