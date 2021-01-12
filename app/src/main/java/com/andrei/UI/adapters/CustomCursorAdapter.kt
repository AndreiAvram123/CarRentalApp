package com.andrei.UI.adapters

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.andrei.carrental.R

class CustomCursorAdapter( context: Context, cursor: Cursor, private val  searchView: SearchView) : androidx.cursoradapter.widget.CursorAdapter(context,cursor,false) {

    private val layoutInflater:LayoutInflater = LayoutInflater.from(context)


    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.suggestion_item_car,parent,false)
    }

    override fun bindView(view: View, context: Context?, cursor: Cursor) {
        val name :String = cursor.getString(cursor.getColumnIndex("name"))
        view.findViewById<TextView>(R.id.tv_car_name).text = name
        view.setOnClickListener{
            searchView.isIconified = true
        }
    }

}