package com.example.countries.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.EventObserver
import com.example.countries.R
import org.koin.android.ext.android.inject

/**
 * A fragment representing a list of Items.
 */
class CountriesFragment : Fragment() {

    private val viewModel: CountriesViewModel by inject()
    private lateinit var countriesAdapter: CountriesRecyclerViewAdapter

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
        val view = inflater.inflate(R.layout.fragment_countries_list, container, false)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                countriesAdapter = CountriesRecyclerViewAdapter { id -> viewModel.onCountryClick(id) }.also {
                    this.adapter = it
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        viewModel.start()
        setupObservers()
    }

    private fun setupNavigation() {
        viewModel.openTaskEvent.observe(this.viewLifecycleOwner, EventObserver { id ->
            openCountryDetails(id)
        })
    }
    private fun setupObservers() {
        viewModel.dataCountries.observe(this.viewLifecycleOwner) {
            countriesAdapter.updateList(it)
            countriesAdapter.notifyDataSetChanged()
            Toast.makeText(this.context, it.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun openCountryDetails(id: String) {
        val action = CountriesFragmentDirections.actionCountriesFragmentToCountryDetailFragment(id)
        findNavController().navigate(action)
    }
}
