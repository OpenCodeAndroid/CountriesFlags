package com.example.countries.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.countries.data.source.local.ModelDao.Country
import com.example.countries.data.source.local.ModelDao.CountryWithCurrencies
import com.example.countries.data.source.local.ModelDao.Currency

/**
 * Data Access Object for the countries table.
 */
@Dao
interface CountriesDao {

    /**
     * Select all countries from the countries table.
     *
     * @return all countries.
     */
    @Transaction
    @Query("SELECT * FROM Countries")
    suspend fun getCountries(): List<Country>

    /**
     * Insert a CountryWithCurrencies in the database. If the country already exists, replace it.
     *
     * @param countryWithCurrenciesList the Country With Currencies to be inserted.
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(countryWithCurrenciesList: List<CountryWithCurrencies>) {
        countryWithCurrenciesList.forEach {
                countryWithCurrencies ->
            insertCountry(countryWithCurrencies.countries)
            countryWithCurrencies.currencies.forEach { currency ->
                insertCurrency(currency)
                insertCountryCurrencyCrossRef(ModelDao.CountryCurrencyCrossRef(countryWithCurrencies.countries.cioc, currency.code))
            }
        }
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountryCurrencyCrossRef(countryCurrencyCrossRef: ModelDao.CountryCurrencyCrossRef)

    /**
     * Insert a country in the database. If the country already exists, replace it.
     *
     * @param country the country to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: Country)

    /**
     * Insert a country in the database. If the country already exists, replace it.
     *
     * @param currency the country to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currency)

    @Transaction
    @Query("SELECT * FROM Countries")
    suspend fun getCountriesWithCurrencies(): List<CountryWithCurrencies>

    /**
     * Select a country by id.
     *
     * @param countryId the country id.
     * @return the country with countryId.
     */

    @Query("SELECT * FROM Countries WHERE cioc = :countryId")
    suspend fun getCountryById(countryId: String): Country?

    /**
     * Update a country.
     *
     * @param country country to be updated
     * @return the number of countries updated. This should always be 1.
     */
    @Transaction
    @Update
    suspend fun updateCountry(country: Country): Int

    /**
     * Delete a country by id.
     *
     * @return the number of countries deleted. This should always be 1.
     */
    @Transaction
    @Query("DELETE FROM Countries WHERE cioc = :countryId")
    suspend fun deleteCountryById(countryId: String): Int

    /**
     * Delete all countries.
     */
    @Transaction
    @Query("DELETE FROM Countries")
    suspend fun deleteCountries()

    /**
     * Delete all currencies.
     */
    @Transaction
    @Query("DELETE FROM Currencies")
    suspend fun deleteCurrencies()

    /**
     * Delete all currencies.
     */
    @Transaction
    @Query("DELETE FROM CountryCurrencyCrossRef")
    suspend fun deleteCountryCurrencyCrossRef()

    @Transaction
    suspend fun deleteAll() {
        deleteCountries()
        deleteCurrencies()
        deleteCountryCurrencyCrossRef()
    }
}
