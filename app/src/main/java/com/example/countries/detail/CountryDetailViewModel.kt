package com.example.countries.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countries.dummy.DummyContent

class CountryDetailViewModel : ViewModel() {

    private val _dataCountry = MutableLiveData<DummyContent.DummyItem>()
    val dataCountry: LiveData<DummyContent.DummyItem> = _dataCountry

    fun start(countryId: String) {
        _dataCountry.value = DummyContent.ITEM_MAP[countryId]
    }
}
