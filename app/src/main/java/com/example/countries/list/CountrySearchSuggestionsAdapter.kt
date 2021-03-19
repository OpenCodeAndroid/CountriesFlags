package com.example.countries.list

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.example.countries.R

class CountrySearchSuggestionsAdapter(context: Context, cursor: Cursor, autoRequery: Boolean) :
    CursorAdapter(context, cursor, autoRequery) {
    override fun newView(context: Context, cursor: Cursor, view: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.country_item_detail, view, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        view.findViewById<TextView>(R.id.contentValue).text = cursor.getString(cursor.getColumnIndexOrThrow("name"))
    }

    fun update(cursor: Cursor) {
        changeCursor(cursor)
        notifyDataSetChanged()
    }
}
