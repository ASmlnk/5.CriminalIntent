package com.bignerdranch.android.a5criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "CrimeListViewModel"

class CrimeListViewModel : ViewModel() {
    private val crimeRepository = CrimeRepository.get()

    private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())
    val crimes: StateFlow<List<Crime>>
        get() = _crimes.asStateFlow()

    init {
        viewModelScope.launch {
            crimeRepository.getCrimes().collect {
                _crimes.value = it
            }
        }
    }

    suspend fun addCrime(crime: Crime) {
        crimeRepository.addCrime(crime)
    }

   fun deleteCrime(crime: Crime) {
        crimeRepository.deleteCrime(crime)
    }
}

/*
suspend fun loadCrimes(): List<Crime> {
    val result = mutableListOf<Crime>()
    delay(5000)
    for (i in 0 until 100) {
        val crime = Crime(
            id = UUID.randomUUID(),
            title = "Crime #$i",
            date = Date(),
            isSolved = i % 2 == 0,
            isPolice = i % 2 == 0
        )
        result += crime
    }
    return result
}*/
