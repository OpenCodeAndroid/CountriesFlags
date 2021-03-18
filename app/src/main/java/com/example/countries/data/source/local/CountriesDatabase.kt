package com.example.countries.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room Database that contains the Countries table.
 */
@Database(entities = [ModelDao.Country::class, ModelDao.Currency::class, ModelDao.CountryCurrencyCrossRef::class], version = 1, exportSchema = false)
abstract class CountriesDatabase : RoomDatabase() {

    abstract fun countryDao(): CountriesDao
}
