package com.bignerdranch.android.a5criminalintent

import java.util.*

data class Crime (
    val id: UUID,
    val title: String,
    val date: Date,
    val isSolved: Boolean,
    val isPolice: Boolean
)
