package com.example.countries.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.domain.GetCountryUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CountryDetailViewModel(val getCountryUseCase: GetCountryUseCase) : ViewModel() {

    private val _dataCountry = MutableLiveData<Country>()
    val dataCountry: LiveData<Country> = _dataCountry

    fun start(countryId: String) {
        viewModelScope.launch {
            handleResult(countryId)
        }
    }

    private suspend fun handleResult(countryId: String) {
        getCountryUseCase.invoke(countryId).collect { result ->
            when (result) {
                is Result.Error -> TODO()
                is Result.Loading -> TODO()
                is Result.Success -> {
                    postIfValid(result)
                }
            }
        }
    }

    private fun postIfValid(result: Result.Success<Country>) {
        val country: Country = result.data
        if (!country.isEmpty) {
            _dataCountry.value = country
        }
    }
}
