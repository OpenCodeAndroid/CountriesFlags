package com.example.countries.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.Event
import com.example.countries.data.source.network.RetrofitBuilder
import com.example.countries.dummy.DummyContent
import com.example.countries.dummy.DummyList
import kotlinx.coroutines.launch

class CountriesViewModel : ViewModel() {

    private val _openTaskEvent = MutableLiveData<Event<String>>()
    val openTaskEvent: LiveData<Event<String>> = _openTaskEvent

    private val _dataCountries = MutableLiveData<DummyList>()
    val dataCountries: LiveData<DummyList> = _dataCountries

    fun start() {
        _dataCountries.value = DummyContent.ITEM_MAP.map { it.value }
        viewModelScope.launch {
            val c = RetrofitBuilder.apiService.getCountries()
            println(" getCountries c $c")
        }
    }

    fun onCountryClick(id: String) {
        _openTaskEvent.value = Event(id)
    }
    // TODO: Implement the ViewModel
}
