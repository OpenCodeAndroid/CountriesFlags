package com.example.countries.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.Event
import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.domain.GetCountriesUseCase
import kotlinx.coroutines.launch

class CountriesViewModel(private val getCountriesUseCase: GetCountriesUseCase) : ViewModel() {

    private val _openCountryEvent = MutableLiveData<Event<String>>()
    val openCountryEvent: LiveData<Event<String>> = _openCountryEvent

    private val _dataCountries = MutableLiveData<List<Country>>()
    val dataCountries: LiveData<List<Country>> = _dataCountries

    fun start() {
        viewModelScope.launch {

            when (val result = getCountriesUseCase.invoke(false)) {
                is Result.Error -> TODO()
                is Result.Loading -> TODO()
                is Result.Success -> _dataCountries.value =
                    result.data.filter { country -> !country.isEmpty }
            }
        }
    }

    fun onCountryClick(id: String) {
        _openCountryEvent.value = Event(id)
    }
    // TODO: Implement the ViewModel
}
