package com.example.countries.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.countries.R
import com.example.countries.data.business.model.Country
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

/**
 * [RecyclerView.Adapter] that can display a [Country].
 * TODO: Replace the implementation with code for your data type.
 */
class CountriesRecyclerViewAdapter(
    private var values: List<Country> = emptyList(),
    val onClick: (String) -> Unit
) : RecyclerView.Adapter<CountriesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        GlideToVectorYou
            .init()
            .with(holder.imageView.context.applicationContext).requestBuilder
            .load(item.flagUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imageView)

        holder.imageView.contentDescription = holder.imageView.context.getString(R.string.country_flag_description, item.name)
        holder.contentView.text = item.name
        holder.cardView.setOnClickListener {
            onClick(item.countryId)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val contentView: TextView = view.findViewById(R.id.content)
        val cardView: CardView = view.findViewById(R.id.cardView)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    fun updateList(list: List<Country>) {
        values = list
    }
}
