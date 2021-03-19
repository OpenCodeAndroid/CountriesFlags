package com.example.countries.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.countries.R
import com.example.countries.data.business.model.Country
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import org.koin.android.ext.android.inject

class CountryDetailFragment : Fragment() {

    private val args: CountryDetailFragmentArgs by navArgs()
    private val viewModel: CountryDetailViewModel by inject()

    lateinit var imageView: ImageView
    lateinit var recycler: RecyclerView
    lateinit var detailRecyclerViewAdapter: DetailRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.country_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.imageView)

        recycler = view.findViewById(R.id.listDetail)
        with(recycler) {
            layoutManager = LinearLayoutManager(context)
            detailRecyclerViewAdapter = DetailRecyclerViewAdapter()
            adapter = detailRecyclerViewAdapter
        }

        viewModel.start(args.countryId)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.dataCountry.observe(this.viewLifecycleOwner) { country ->
            populate(country)
        }
    }

    fun populate(country: Country) {
        GlideToVectorYou
            .init()
            .with(imageView.context.applicationContext).requestBuilder
            .load(country.flagUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(imageView)

        imageView.contentDescription =
            imageView.context.getString(R.string.country_flag_description, country.name)

        detailRecyclerViewAdapter.updateList(createDetailList(imageView.context, country))
        detailRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun createDetailList(context: Context, country: Country): List<DetailItems> {
        val mutableListDetailItems: MutableList<DetailItems> = mutableListOf()
        if (country.currencies.size > 1) {
            mutableListDetailItems.add(
                DetailItems(context.getString(R.string.currencies_description_size, country.name, country.currencies.size.toString()), ""))
        } else {
            mutableListDetailItems.add(
                DetailItems(context.getString(R.string.currency_description_size_one, country.name), ""))
        }

        country.currencies.forEach { currency ->
            mutableListDetailItems.add(
                DetailItems(context.getString(R.string.currencies_description_code, currency.name, currency.symbol), ""))
        }

        context.getString(R.string.country_flag_description, country.name)
        return mutableListDetailItems
    }
}
