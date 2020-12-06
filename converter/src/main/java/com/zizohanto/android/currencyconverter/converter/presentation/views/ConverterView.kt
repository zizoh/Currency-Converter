package com.zizohanto.android.currencyconverter.converter.presentation.views

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.tabs.TabLayoutMediator
import com.zizohanto.android.currencyconverter.converter.R
import com.zizohanto.android.currencyconverter.converter.databinding.LayoutConverterBinding
import com.zizohanto.android.currencyconverter.converter.presentation.models.SymbolItem
import com.zizohanto.android.currencyconverter.converter.presentation.mvi.ConverterViewIntent
import com.zizohanto.android.currencyconverter.converter.presentation.mvi.ConverterViewState
import com.zizohanto.android.currencyconverter.converter.ui.converter.ChartFragment
import com.zizohanto.android.currencyconverter.converter.ui.converter.ChartFragment.ChartFragmentBundleData
import com.zizohanto.android.currencyconverter.converter.ui.converter.ViewPagerAdapter
import com.zizohanto.android.currencyconverter.core.ext.makeLinks
import com.zizohanto.android.currencyconverter.presentation.mvi.MVIView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.currency_layout.view.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import reactivecircus.flowbinding.android.view.clicks

/**
 * Created by zizoh on 26/November/2020.
 */

@AndroidEntryPoint
class ConverterView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet),
    MVIView<ConverterViewIntent, ConverterViewState> {

    private var binding: LayoutConverterBinding

    lateinit var fragment: Fragment

    private val viewPagerAdapter by lazy { ViewPagerAdapter(fragment) }

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
                val spannable = SpannableString("Currency\nCalculator.")
                spannable.setSpan(
                    ForegroundColorSpan(Color.GREEN),
                    spannable.length - 1, spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.currencyConverter.text = spannable
                val symbolItems: List<SymbolItem> = getSymbolItems(state.state.symbols)
                val adapter = SymbolAdapter(context, R.layout.currency_layout, symbolItems)
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
                            binding.base.setSymbol(symbolItem.symbol)

                            val isValidAmount = binding.base.getAmount().isNotEmpty()
                            val areDifferentCurrencies =
                                binding.target.getSymbol() != symbolItem.symbol
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
                            binding.target.setSymbol(symbolItem.symbol)

                            val isValidAmount = binding.base.getAmount().isNotEmpty()
                            val areDifferentCurrencies =
                                binding.target.getSymbol() != symbolItem.symbol
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

                binding.base.et_amount.doOnTextChanged { text: CharSequence?, _, _, _ ->
                    val enableButton = enableConvertButton(
                        text,
                        binding.base.getSymbol(),
                        binding.target.getSymbol()
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
                binding.target.setAmount(state.historicalData.convertedRate.toString())

                val marketRateText = "Mid-market exchange rate at ${state.historicalData.time}"
                binding.marketRate.text = marketRateText
                binding.marketRate.makeLinks(
                    Pair(marketRateText, OnClickListener {
                        showDummyToast()
                    })
                )
            }
            is ConverterViewState.Error -> {
                if (!state.isErrorGettingSymbols) {
                    enableConvertButton(false)
                    showConversionProgress(false)
                }
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            ConverterViewState.GettingChartData -> {
                Toast.makeText(context, "Getting chart data", Toast.LENGTH_SHORT).show()
                viewPagerAdapter.clearFragments()
            }
            is ConverterViewState.ChartDataLoaded -> {
                val chartFragment: Fragment = getChartFragment(state)
                viewPagerAdapter.addFragment(state.numberOfEntries, chartFragment)
                binding.tabViewpager.offscreenPageLimit = 2
                binding.tabViewpager.adapter = viewPagerAdapter
                TabLayoutMediator(binding.tabLayout, binding.tabViewpager) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = "Past 30 days"
                        }
                        1 -> {
                            tab.text = "Past 90 days"
                        }
                    }
                }.attach()

                val getRateAlerts = context.getString(R.string.get_rates_alert)
                binding.tvGetRateAlerts.makeLinks(
                    Pair(getRateAlerts, OnClickListener {
                        showDummyToast()
                    })
                )
            }
        }
    }

    private fun showDummyToast() {
        Toast.makeText(
            context,
            context.getString(R.string.nothing_to_see_here),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun getChartFragment(state: ConverterViewState.ChartDataLoaded): Fragment {
        val bundle = ChartFragmentBundleData(
            state.numberOfEntries,
            binding.base.getSymbol(),
            state.historicalData
        )
        return ChartFragment.newInstance(bundle)
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
        binding.target.setAmount("")
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
            .transform {
                val amount = binding.base.getAmount().toDouble()
                val base = binding.base.getSymbol()
                val target = binding.target.getSymbol()
                emit(ConverterViewIntent.GetRates(amount, base, target))
                emit(ConverterViewIntent.GetChartData(30, base, target))
                emit(ConverterViewIntent.GetChartData(90, base, target))
            }

    override val intents: Flow<ConverterViewIntent>
        get() = getRateIntent
}