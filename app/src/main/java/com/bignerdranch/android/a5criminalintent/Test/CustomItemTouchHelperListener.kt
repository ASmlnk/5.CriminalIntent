package com.bignerdranch.android.a5criminalintent.Test

interface CustomItemTouchHelperListener {

    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemSwiped(position: Int)

    fun onItemClear(position: Int)

}