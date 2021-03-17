package com.example.countries.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.countries.R

class CountryDetailFragment : Fragment() {

    private val args: CountryDetailFragmentArgs by navArgs()
    private lateinit var viewModel: CountryDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.country_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CountryDetailViewModel::class.java)
        viewModel.start(args.countryId)
        setupObservers()
    }
    private fun setupObservers() {
        viewModel.dataCountry.observe(this.viewLifecycleOwner) {
            Toast.makeText(this.context, it.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
