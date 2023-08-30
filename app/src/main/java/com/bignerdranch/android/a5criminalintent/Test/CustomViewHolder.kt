package com.bignerdranch.android.a5criminalintent.Test

import android.content.Context
import android.view.GestureDetector
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.a5criminalintent.R

/*
class CustomViewHolder: RecyclerView.ViewHolder, View.OnTouchListener, GestureDetector.OnGestureListener {
    companion object {
        private var TAG: String = CustomViewHolder::class.java.simpleName
    }

    */
/*Main*//*

    //private lateinit var context: Context
    private lateinit var customListeners: CustomListeners
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var gestureDetector: GestureDetector
    */
/*Date*//*

    //private lateinit var view: View
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    private lateinit var cardView: CardView
    private lateinit var item: CustomViewModel

    constructor(context: Context, itemView: View, customListeners: CustomListeners, itemTouchHelper: ItemTouchHelper) : super() {
        this.customListeners = customListeners
        this.itemTouchHelper = itemTouchHelper
    }

    init {
        imageView = itemView.findViewById(R.id.image_view)
        textView = itemView.findViewById(R.id.text_view)
        cardView = itemView.findViewById(R.id.card_view)
        gestureDetector = GestureDetector(imageView.context, this)
    }




}*/
