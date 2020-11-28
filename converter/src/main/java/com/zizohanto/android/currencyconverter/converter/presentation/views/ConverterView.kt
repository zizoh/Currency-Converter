package com.zizohanto.android.currencyconverter.converter.presentation.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
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

                binding.spinnerBaseCurrency.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val symbolItem: SymbolItem =
                                parent?.getItemAtPosition(position) as SymbolItem
                            binding.tvBaseCurrency.text = symbolItem.symbol

                            val isValidAmount = !binding.amount.text.isNullOrEmpty()
                            val areDifferentCurrencies =
                                binding.tvTargetCurrency.text.toString() != symbolItem.symbol
                            enableConvertButton(isValidAmount && areDifferentCurrencies)
                            clearConvertedValue()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }

                binding.spinnerTargetCurrency.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val symbolItem: SymbolItem =
                                parent?.getItemAtPosition(position) as SymbolItem
                            binding.tvTargetCurrency.text = symbolItem.symbol

                            val isValidAmount = !binding.amount.text.isNullOrEmpty()
                            val areDifferentCurrencies =
                                binding.tvBaseCurrency.text.toString() != symbolItem.symbol
                            enableConvertButton(isValidAmount && areDifferentCurrencies)
                            clearConvertedValue()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }

                binding.swap.setOnClickListener {
                    val baseSelectedItem: Int = binding.spinnerBaseCurrency.selectedItemPosition
                    val targetSelectedItem: Int = binding.spinnerTargetCurrency.selectedItemPosition

                    binding.spinnerBaseCurrency.setSelection(targetSelectedItem)
                    binding.spinnerTargetCurrency.setSelection(baseSelectedItem)
                    clearConvertedValue()
                }

                binding.amount.doOnTextChanged { text: CharSequence?, _, _, _ ->
                    val enableButton = enableConvertButton(
                        text,
                        binding.tvBaseCurrency.text.toString(),
                        binding.tvTargetCurrency.text.toString()
                    )
                    enableConvertButton(enableButton)
                    clearConvertedValue()
                }
            }
            is ConverterViewState.GettingConversion -> {
                enableConvertButton(false)
                showConversionProgress(true)
            }
            is ConverterViewState.Converted -> {
                enableConvertButton(true)
                showConversionProgress(false)
                binding.converted.text = state.historicalData.convertedRate.toString()
            }
            is ConverterViewState.Error -> {
                if (!state.isErrorGettingSymbols) {
                    enableConvertButton(false)
                    showConversionProgress(false)
                }
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enableConvertButton(
        text: CharSequence?,
        baseSymbol: String,
        targetSymbol: String
    ): Boolean {
        val isValidAmount = !text.isNullOrEmpty()
        val areDifferentCurrencies = baseSymbol != targetSymbol
        return isValidAmount && areDifferentCurrencies
    }

    private fun showConversionProgress(showProgress: Boolean) {
        if (showProgress) {
            binding.btnConvert.showProgress {
                buttonTextRes = R.string.converting
                progressColor = Color.WHITE
            }
        } else {
            binding.btnConvert.hideProgress(R.string.convert)
        }
    }

    private fun enableConvertButton(enable: Boolean) {
        binding.btnConvert.isEnabled = enable
    }

    private fun clearConvertedValue() {
        binding.converted.text = ""
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
        get() = binding.btnConvert.clicks()
            .map {
                ConverterViewIntent.GetRates(
                    binding.amount.text.toString().toDouble(),
                    binding.tvBaseCurrency.text.toString(),
                    binding.tvTargetCurrency.text.toString()
                )
            }

    override val intents: Flow<ConverterViewIntent>
        get() = getRateIntent
}