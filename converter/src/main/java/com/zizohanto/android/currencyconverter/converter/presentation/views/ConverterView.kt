package com.zizohanto.android.currencyconverter.converter.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import com.zizohanto.android.currencyconverter.converter.R
import com.zizohanto.android.currencyconverter.converter.databinding.LayoutConverterBinding
import com.zizohanto.android.currencyconverter.converter.presentation.models.SymbolItem
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
                val symbolItems: List<SymbolItem> = getSymbolItems(state.state.symbols)
                val adapter = SymbolAdapter(context, R.layout.currency_button_layout, symbolItems)
                binding.spinnerBaseCurrency.adapter = adapter
                binding.spinnerTargetCurrency.adapter = adapter

                val baseSpinnerItemSelectListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val symbolItem: SymbolItem =
                            parent?.getItemAtPosition(position) as SymbolItem
                        binding.tvBaseCurrency.text = symbolItem.symbol
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
                binding.spinnerBaseCurrency.onItemSelectedListener = baseSpinnerItemSelectListener

                val targetSpinnerItemSelectListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val symbolItem: SymbolItem =
                            parent?.getItemAtPosition(position) as SymbolItem
                        binding.tvTargetCurrency.text = symbolItem.symbol
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
                binding.spinnerTargetCurrency.onItemSelectedListener =
                    targetSpinnerItemSelectListener

                binding.swap.setOnClickListener {
                    val baseSelectedItem: Int = binding.spinnerBaseCurrency.selectedItemPosition
                    val targetSelectedItem: Int = binding.spinnerTargetCurrency.selectedItemPosition

                    binding.spinnerBaseCurrency.setSelection(targetSelectedItem)
                    binding.spinnerTargetCurrency.setSelection(baseSelectedItem)
                }
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

    private fun getSymbolItems(symbols: List<String>): List<SymbolItem> {
        val symbolItems = mutableListOf<SymbolItem>()
        symbols.sorted().forEach {
            val drawableResId = getFlagDrawableResourceId(it)
            symbolItems.add(SymbolItem(drawableResId, it))
        }
        return symbolItems
    }

    private fun getFlagDrawableResourceId(it: String): Int {
        var drawableResId =
            resources.getIdentifier("flag_${it.toLowerCase()}", "drawable", context.packageName)
        if (drawableResId == 0) {
            drawableResId =
                resources.getIdentifier("flag_placeholder", "drawable", context.packageName)
        }
        return drawableResId
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