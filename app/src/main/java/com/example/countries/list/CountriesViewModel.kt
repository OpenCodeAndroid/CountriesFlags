package com.example.countries.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.Event
import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.domain.GetCountriesUseCase
import com.example.countries.data.domain.SearchCountriesUseCase
import kotlinx.coroutines.launch

class CountriesViewModel(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val searchCountriesUseCase: SearchCountriesUseCase
) : ViewModel() {

    private val _openCountryEvent = MutableLiveData<Event<String>>()
    val openCountryEvent: LiveData<Event<String>> = _openCountryEvent

    private val _dataCountries = MutableLiveData<List<Country>>()
    val dataCountries: LiveData<List<Country>> = _dataCountries

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    fun start() {
        loadAll()
    }

    private fun loadAll() {
        viewModelScope.launch {

            when (val result = getCountriesUseCase.invoke(false)) {
                is Result.Error -> _showLoading.value = false
                is Result.Loading -> _showLoading.value = true
                is Result.Success -> {
                    _dataCountries.value = result.data.filter { country -> !country.isEmpty }
                    _showLoading.value = false
                }
            }
        }
    }

    fun onCountryClick(id: String) {
        _openCountryEvent.value = Event(id)
    }

    fun onQueryTextSubmit(query: String?) {
        handleSearchCountry(query)
    }

    fun onQueryTextChange(newText: String?) {
        handleSearchCountry(newText)
    }

    private fun handleSearchCountry(query: String?) {
        if (query.isNullOrEmpty()) {
            loadAll()
        } else {
            search(query)
        }
    }

    private fun search(query: String) {
        viewModelScope.launch {
            when (val result = searchCountriesUseCase.invoke(query, false)) {
                is Result.Error ->
                    _showLoading.value = false
                is Result.Loading ->
                    _showLoading.value = true
                is Result.Success -> {
                    _dataCountries.value = result.data.filter { country ->
                        !country.isEmpty
                    }
                }
            }
        }
    }
    // TODO: Implement the ViewModel
}
