package com.screamxo.Adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.screamxo.Model.CountryModel
import com.screamxo.R
import kotlinx.android.synthetic.main.country_item.view.*


class CountriesAdapter(ctx: Context,
                       countries: List<CountryModel>) :
        ArrayAdapter<CountryModel>(ctx, 0, countries) {
    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val country = getItem(position)
        val view = recycledView ?: LayoutInflater.from(context).inflate(
                R.layout.country_item,
                parent,
                false
        )


        view.img_flag.setImageResource( R.drawable.flag_ae)
        view.img_flag.setImageResource(context.resources.getIdentifier("flag_" + country.iso2.toLowerCase(), "drawable", context.getPackageName()))
        view.tv_name.text = country.name
        return view
    }
}