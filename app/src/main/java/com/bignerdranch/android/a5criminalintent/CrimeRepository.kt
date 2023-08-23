package com.bignerdranch.android.a5criminalintent

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.a5criminalintent.DataBase.CrimeDataBase
import kotlinx.coroutines.flow.Flow
import java.util.*

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {

    private val database: CrimeDataBase = Room
        .databaseBuilder(
            context.applicationContext,
            CrimeDataBase::class.java,
            DATABASE_NAME
        )
        .build()

    fun getCrimes(): Flow<List<Crime>> = database.crimeDao().getCrimes()
    suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}