package com.zizohanto.android.currencyconverter.converter.presentation.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zizohanto.android.currencyconverter.converter.R
import com.zizohanto.android.currencyconverter.converter.presentation.models.SymbolItem
import com.zizohanto.android.currencyconverter.core.ext.getImage
import de.hdodenhof.circleimageview.CircleImageView


/**
 * Created by zizoh on 27/November/2020.
 */

class SymbolAdapter(
    context: Context,
    textViewResourceId: Int,
    private val symbolItems: List<SymbolItem>
) : ArrayAdapter<SymbolItem>(context, textViewResourceId, symbolItems) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, converterView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val layout: View = inflater.inflate(R.layout.currency_button_layout, parent, false)
        val symbolItem = symbolItems[position]
        val flag: CircleImageView = layout.findViewById(R.id.currency_flag)
        flag.setImageDrawable(parent.context.getImage(symbolItem.flagResId))

        val symbol: TextView = layout.findViewById(R.id.currency_symbol)
        symbol.text = symbolItem.symbol

        return layout
    }

}