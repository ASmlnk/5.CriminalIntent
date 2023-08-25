package com.bignerdranch.android.a5criminalintent.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomOpenHelper
import androidx.room.TypeConverters
import com.bignerdranch.android.a5criminalintent.Crime

@Database(entities = [Crime::class], version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDataBase : RoomDatabase() {
    abstract fun crimeDao(): CrimeDao
}