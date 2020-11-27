package com.zizohanto.android.currencyconverter.converter.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import com.zizohanto.android.currencyconverter.converter.databinding.LayoutConverterBinding
import com.zizohanto.android.currencyconverter.converter.presentation.mvi.ConverterViewIntent
import com.zizohanto.android.currencyconverter.converter.presentation.mvi.ConverterViewState
import com.zizohanto.android.currencyconverter.presentation.mvi.MVIView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import reactivecircus.flowbinding.android.view.clicks

/**
 * Created by zizoh on 26/November/2020.
 */

@AndroidEntryPoint
class ConverterView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet),
    MVIView<ConverterViewIntent, ConverterViewState> {

    private var binding: LayoutConverterBinding

    init {
        isSaveEnabled = true
        val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = LayoutConverterBinding.inflate(inflater, this, true)
    }

    override fun render(state: ConverterViewState) {
        when (state) {
            ConverterViewState.Idle -> {
            }
            ConverterViewState.GettingSymbols -> {
                Toast.makeText(context, "Getting symbols", Toast.LENGTH_SHORT).show()
            }
            is ConverterViewState.SymbolsLoaded -> {
                val baseCurrency = state.state.symbols.random()
                binding.btnBaseCurrency.setCurrencySymbol(baseCurrency)
                binding.tvBaseCurrency.text = baseCurrency
                val targetCurrency = state.state.symbols.random()
                binding.btnTargetCurrency.setCurrencySymbol(targetCurrency)
                binding.tvTargetCurrency.text = targetCurrency
            }
            is ConverterViewState.GettingRates -> {
                Toast.makeText(context, "Getting rates", Toast.LENGTH_SHORT).show()
            }
            is ConverterViewState.Converted -> {
                binding.converted.text = state.state.convertedRate.toString()
            }
            is ConverterViewState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val getRateIntent: Flow<ConverterViewIntent>
        get() = binding.btnConvert.clicks().map {
            ConverterViewIntent.GetRates(
                binding.amount.text.toString().toDouble(),
                binding.tvBaseCurrency.text.toString(),
                binding.tvTargetCurrency.text.toString()
            )
        }

    override val intents: Flow<ConverterViewIntent>
        get() = getRateIntent
}