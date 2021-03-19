package com.example.countries.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.data.business.model.Country

/**
 * [RecyclerView.Adapter] that can display a [Country].
 * TODO: Replace the implementation with code for your data type.
 */
class DetailRecyclerViewAdapter(
    private var values: List<String> = emptyList()
) : RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_item_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.labelTextView.text = item
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val labelTextView: TextView = view.findViewById(R.id.contentLabel)
    }

    fun updateList(list: List<String>) {
        values = list
        notifyDataSetChanged()
    }
}

data class DetailItems(val fieldLabel: String, val fieldValue: String)
