/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.countries.data.source

import com.example.countries.data.Result
import com.example.countries.data.Result.Error
import com.example.countries.data.Result.Success
import com.example.countries.data.business.model.Country
import java.lang.NullPointerException

class FakeDataSource(var countries: MutableList<Country>? = mutableListOf()) : CountriesDataSource {
    override suspend fun getCountries(): Result<List<Country>> {
        countries?.let { return Success(it) }
        return Error(
            Exception("Countries not found")
        )
    }

    override suspend fun getCountry(countryId: String): Result<Country> {
        val item = countries?.first { it.countryId == countryId }
        return if (item == null) {
            Error(NullPointerException("Country not found"))
        } else {
            Success(item)
        }
    }

    override suspend fun getCountryByName(name: String): Result<Country> {
        val item = countries?.first { it.name == name }
        return if (item == null) {
            Error(NullPointerException("Country not found"))
        } else {
            Success(item)
        }
    }

    override suspend fun getCountriesByName(name: String): Result<List<Country>> {
        val item = countries?.filter { it.name.contains(name) }
        return if (item == null) {
            Error(NullPointerException("Country not found"))
        } else {
            Success(item)
        }
    }

    override suspend fun getCountryByIsoCode(isoCode: String): Result<Country> {
        val item = countries?.first { it.isoCode.contains(isoCode) }
        return if (item == null) {
            Error(NullPointerException("Country not found"))
        } else {
            Success(item)
        }
    }

    override suspend fun save(countryList: List<Country>) {
        countries?.addAll(countryList)
    }

    override suspend fun saveCountry(country: Country) {
        countries?.add(country)
    }

    override suspend fun deleteAllCountries() {
        countries?.clear()
    }
}
