package com.example.countries.data.source.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

class ModelDao {
    @Entity(tableName = "countries")
    data class Country @JvmOverloads constructor(
        @PrimaryKey var cioc: String = UUID.randomUUID().toString(),
        var name: String = "",
        var capital: String = "",
        var flag: String = ""
    )

    // https://developer.android.com/training/data-storage/room/relationships
    data class CountryWithCurrencies @JvmOverloads constructor(
        @Embedded val countries: Country = Country(),
        @Relation(
                parentColumn = "cioc",
                entityColumn = "code",
                associateBy = Junction(CountryCurrencyCrossRef::class)
        )
        var currencies: List<Currency> = emptyList()
    )

    @Entity(primaryKeys = ["cioc", "code"])
    data class CountryCurrencyCrossRef(
        val cioc: String,
        val code: String
    )

    @Entity(tableName = "currencies")
    data class Currency(
        @PrimaryKey val code: String = UUID.randomUUID().toString(),
        val name: String,
        val symbol: String
    )
}
