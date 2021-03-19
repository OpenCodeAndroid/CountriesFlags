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
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the countries table.
 * [CountryWithCurrencies] is the full object representation at the moment
 * [ModelDao.Country] model does not include out of the box data from currencies
 * the most used methods as API have a relation with [CountryWithCurrencies]
 */
@Dao
interface CountriesDao {

    /**
     * Insert a List of CountryWithCurrencies in the database. If the country already exists, replace it.
     *
     * @param countryWithCurrenciesList the Country With Currencies List to be inserted.
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(countryWithCurrenciesList: List<CountryWithCurrencies>) {
        countryWithCurrenciesList.forEach { countryWithCurrencies ->
            insertCountry(countryWithCurrencies)
        }
    }

    /**
     * Insert a List of CountryWithCurrencies in the database. If the country already exists, replace it.
     *
     * @param countryWithCurrenciesList the Country With Currencies List to be inserted.
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(countryWithCurrencies: CountryWithCurrencies) {
        val country = countryWithCurrencies.countries
        insertCountry(country)
        countryWithCurrencies.currencies.forEach { currency ->
            insertCurrenciesAndLinkToCountry(currency, country.countryId)
        }
    }
    private suspend fun insertCurrenciesAndLinkToCountry(
        currency: Currency,
        countryId: String
    ) {
        insertCurrency(currency)
        insertCountryCurrencyCrossRef(
            ModelDao.CountryCurrencyCrossRef(
                countryId,
                currency.currencyId
            )
        )
    }

    @Transaction
    suspend fun deleteAll() {
        deleteCountries()
        deleteCurrencies()
        deleteCountryCurrencyCrossRef()
    }

    suspend fun getCountriesByName(name: String) =
        getCountriesWithCurrenciesByName(name).map {
            it.mapToModel()
        }
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountryCurrencyCrossRef(countryCurrencyCrossRef: ModelDao.CountryCurrencyCrossRef)

    /**
     * Select all countries from the countries table.
     *
     * @return all countries.
     */
    @Transaction
    @Query("SELECT * FROM Countries")
    fun getCountries(): Flow<List<Country>>

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
    @Transaction
    @Query("SELECT * FROM Countries WHERE countryId = :countryId")
    suspend fun getCountryWithCurrenciesById(countryId: String): CountryWithCurrencies?

    @Transaction
    suspend fun getCountryById(countryId: String) =
        getCountryWithCurrenciesById(countryId)?.mapToModel()

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
    @Query("DELETE FROM Countries WHERE countryId = :countryId")
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

    /**
     * Query se a Countries by name.
     * @param name the country name to be selecte
     * @return list of Country where the name is name.
     */
    @Transaction
    @Query("SELECT * FROM Countries WHERE name = :name")
    suspend fun getCountryByName(name: String): List<CountryWithCurrencies>

    /**
     * Query se a Countries by name.
     * @param name the country name to be selected
     * @return list of Country where the name is name.
     */
    @Transaction // TODO
    @Query("SELECT * FROM Countries WHERE name LIKE '%' || :name || '%'")
    suspend fun getCountriesWithCurrenciesByName(name: String): List<CountryWithCurrencies>
}
