package com.zizohanto.android.currencyconverter.converter.presentation.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.zizohanto.android.currencyconverter.converter.R
import com.zizohanto.android.currencyconverter.converter.databinding.CurrencyLayoutBinding

/**
 * Created by zizoh on 06/December/2020.
 */

class CurrencyView : LinearLayout {

    private var binding: CurrencyLayoutBinding

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = CurrencyLayoutBinding.inflate(inflater, this, true)

        val attributes: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CurrencyView, 0, 0)

        val amountIsEditable: Boolean =
            attributes.getBoolean(R.styleable.CurrencyView_isEditable, true)

        binding.etAmount.isEnabled = amountIsEditable
    }

    fun setAmount(emptyStateCaption: String?) {
        if (emptyStateCaption != null) {
            binding.etAmount.setText(emptyStateCaption)
        }
    }

    fun getAmount(): String = binding.etAmount.text.toString()

    fun setSymbol(emptyStateCaption: String?) {
        if (emptyStateCaption != null) {
            binding.tvSymbol.text = emptyStateCaption
        }
    }

    fun getSymbol(): String = binding.tvSymbol.text.toString()

}