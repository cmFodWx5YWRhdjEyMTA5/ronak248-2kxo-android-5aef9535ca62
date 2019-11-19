package com.screamxo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.screamxo.Model.CountryModel
import com.screamxo.Model.StateModel
import com.screamxo.R
import kotlinx.android.synthetic.main.country_item.view.*


class StatesAdapter(ctx: Context,
                    moods: List<StateModel>) :
        ArrayAdapter<StateModel>(ctx, 0, moods) {
    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val mood = getItem(position)
        val view = recycledView ?: LayoutInflater.from(context).inflate(
                R.layout.country_item,
                parent,
                false
        )
        view.img_flag.visibility=View.GONE
        view.tv_name.text = mood.name
        return view
    }
}