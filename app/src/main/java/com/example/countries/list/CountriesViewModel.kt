package com.example.countries.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.Event
import com.example.countries.data.domain.GetCountriesUseCase
import com.example.countries.dummy.DummyContent
import com.example.countries.dummy.DummyList
import kotlinx.coroutines.launch

class CountriesViewModel(private val getCountriesUseCase: GetCountriesUseCase) : ViewModel() {

    private val _openCountryEvent = MutableLiveData<Event<String>>()
    val openCountryEvent: LiveData<Event<String>> = _openCountryEvent

    private val _dataCountries = MutableLiveData<DummyList>()
    val dataCountries: LiveData<DummyList> = _dataCountries

    fun start() {
        _dataCountries.value = DummyContent.ITEM_MAP.map { it.value }
        viewModelScope.launch {
            val c = getCountriesUseCase.invoke(true)
            println(" getCountries c $c")
        }
    }

    fun onCountryClick(id: String) {
        _openCountryEvent.value = Event(id)
    }
    // TODO: Implement the ViewModel
}
