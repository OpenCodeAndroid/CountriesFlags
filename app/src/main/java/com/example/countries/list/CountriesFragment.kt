package com.example.countries.list

import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.EventObserver
import com.example.countries.R
import com.example.countries.data.business.model.Country
import org.koin.android.ext.android.inject

/**
 * A fragment representing a list of Items.
 */
class CountriesFragment : Fragment() {

    private val viewModel: CountriesViewModel by inject()
    private lateinit var countriesAdapter: CountriesRecyclerViewAdapter
    private lateinit var progress: ProgressBar
    private lateinit var searchView: SearchView
    private lateinit var suggestionsAdapter: CountrySearchSuggestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_countries_list, container, false)
        // Set the adapter
        progress = view.findViewById(R.id.loading_spinner)
        searchView = view.findViewById(R.id.searchView)
        with(view.findViewById<RecyclerView>(R.id.list)) {
            layoutManager = LinearLayoutManager(context)
            countriesAdapter =
                CountriesRecyclerViewAdapter { id -> viewModel.onCountryClick(id) }.also {
                    this.adapter = it
                }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewSetup()
        setupNavigation()
        viewModel.start()
        setupObservers()
    }

    private fun searchViewSetup() {
        searchView.suggestionsAdapter = CountrySearchSuggestionsAdapter(this.requireContext(), getCursor(listOf()), true).also {
            suggestionsAdapter = it
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // Called when the user submits the query.
            override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.onQueryTextSubmit(query)
                    return false
            }

            // Called when the query text is changed by the user.
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onQueryTextChange(newText)
                return false
            }
        })
    }

    private fun getCursor(listOf: List<Country>): Cursor {
        val columns = arrayOf<String>("_id", "name")
        val to = intArrayOf(R.id.contentLabel)
        val matrixCursor: MatrixCursor = MatrixCursor(columns)

        // startManagingCursor(matrixCursor);
        // fun startManagingCursor(matrixCursor: MatrixCursor) {
        //     matrixCursor.addRow(arrayOf<Any>(1,"Danie Nogueira"))
        //     matrixCursor.addRow(arrayOf<Any>(2,"Daniela Nogueira Bomzinho da Silva"))
        //     matrixCursor.addRow(arrayOf<Any>(3,"Gabirela MArcela"))
        //     matrixCursor.addRow(arrayOf<Any>(4,"Android Natural Nogueira"))
        // }

        listOf.forEach {
            matrixCursor.addRow(arrayOf<Any>(it.countryId, it.name))
        }
        return matrixCursor
    }

    private fun setupNavigation() {
        viewModel.openCountryEvent.observe(this.viewLifecycleOwner, EventObserver { id ->
            openCountryDetails(id)
        })
    }

    private fun setupObservers() {
        viewModel.dataCountries.observe(this.viewLifecycleOwner) {
            countriesAdapter.updateList(it)
            countriesAdapter.notifyDataSetChanged()
        }
        viewModel.showLoading.observe(this.viewLifecycleOwner) {
            progress.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        viewModel.dataSearchCountries.observe(this.viewLifecycleOwner) {
            suggestionsAdapter.update(getCursor(it))
        }
    }

    private fun openCountryDetails(id: String) {
        val action = CountriesFragmentDirections.actionCountriesFragmentToCountryDetailFragment(id)
        findNavController().navigate(action)
    }
}
