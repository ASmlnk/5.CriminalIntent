package com.bignerdranch.android.a5criminalintent

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.a5criminalintent.DataBase.CrimeDataBase
import com.bignerdranch.android.a5criminalintent.DataBase.migration_1_2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {

    private val database: CrimeDataBase = Room
        .databaseBuilder(
            context.applicationContext,
            CrimeDataBase::class.java,
            DATABASE_NAME
        )
        .addMigrations(migration_1_2)
        .build()

    fun getCrimes(): Flow<List<Crime>> = database.crimeDao().getCrimes()

    suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)

    fun updateCrime(crime: Crime) {
        coroutineScope.launch {
            database.crimeDao().updateCrime(crime)
        }
    }

    suspend fun addCrime(crime: Crime) {
        database.crimeDao().addCrime(crime)
    }

     fun deleteCrime(crime: Crime) {
         coroutineScope.launch {
             database.crimeDao().deleteCrime(crime)
         }
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}