package com.bignerdranch.android.a5criminalintent

import android.icu.text.DateFormat
import androidx.lifecycle.ViewModel
import java.util.*

class CrimeListViewModel: ViewModel() {

    val crimes = mutableListOf<Crime>()

    init {
        for (i in 0 until 100) {
            val crime = Crime(
                id = UUID.randomUUID(),
                title = "Crime #$i",
                date = Date(),
                isSolved = i % 2 == 0,
                isPolice = i % 2 == 0
            )

            crimes += crime
        }
    }
}