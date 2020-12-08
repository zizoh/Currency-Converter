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
import com.google.android.material.snackbar.Snackbar
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
import com.zizohanto.android.currencyconverter.core.ext.safeOffer
import com.zizohanto.android.currencyconverter.core.ext.show
import com.zizohanto.android.currencyconverter.core.ext.showSnackbar
import com.zizohanto.android.currencyconverter.presentation.mvi.MVIView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.currency_layout.view.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.merge
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

    private var retryGetSymbolsListener: OnClickListener? = null

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
                showCurrencyCalculator()

                binding.cvSpinnerBaseCurrency.show(false)
                binding.cvSpinnerTargetCurrency.show(false)
                binding.swap.show(false)
                binding.cvSpinnerTargetCurrencyShimmer.show(true)
                binding.cvSpinnerBaseCurrencyShimmer.show(true)
                binding.cvSpinnerTargetCurrencyShimmer.showShimmer(true)
                binding.cvSpinnerBaseCurrencyShimmer.showShimmer(true)
            }
            is ConverterViewState.SymbolsLoaded -> {
                val symbolItems: List<SymbolItem> = getSymbolItems(state.state.symbols)
                val adapter = SymbolAdapter(context, R.layout.currency_layout, symbolItems)
                binding.spinnerBaseCurrency.adapter = adapter
                binding.spinnerTargetCurrency.adapter = adapter

                binding.cvSpinnerBaseCurrency.show(true)
                binding.cvSpinnerTargetCurrency.show(true)
                binding.swap.show(true)

                binding.cvSpinnerTargetCurrencyShimmer.stopShimmer()
                binding.cvSpinnerBaseCurrencyShimmer.stopShimmer()
                binding.cvSpinnerTargetCurrencyShimmer.show(false)
                binding.cvSpinnerBaseCurrencyShimmer.show(false)

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

                            val isValidAmount = binding.base.getAmount().isNotEmpty()
                            val areDifferentCurrencies =
                                binding.target.getSymbol() != symbolItem.symbol
                            binding.base.setSymbol(symbolItem.symbol)
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

                            val isValidAmount = binding.base.getAmount().isNotEmpty()
                            val areDifferentCurrencies =
                                binding.target.getSymbol() != symbolItem.symbol
                            binding.target.setSymbol(symbolItem.symbol)
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
                binding.target.setAmount(state.historicalData.convertedRate.toString())

                val marketRateText = "Mid-market exchange rate at ${state.historicalData.time}"
                binding.marketRate.text = marketRateText
                binding.marketRate.makeLinks(
                    Pair(marketRateText, OnClickListener {
                        showDummyToast()
                    })
                )
                binding.llMarketRate.setOnClickListener {
                    showDummyToast()
                }
                binding.tvEmptyState.show(false)
                binding.tabLayout.show(true)
                binding.tabViewpager.show(true)
                binding.llMarketRate.show(true)
            }
            is ConverterViewState.Error -> {
                when (state) {
                    is ConverterViewState.Error.ErrorGettingSymbols -> {
                        showCurrencyCalculator()
                        enableConvertButton(false)
                        showConversionProgress(false)
                        val symbolItems: List<SymbolItem> = getSymbolItems(listOf("N/A"))
                        val adapter = SymbolAdapter(context, R.layout.currency_layout, symbolItems)
                        binding.spinnerBaseCurrency.adapter = adapter
                        binding.spinnerTargetCurrency.adapter = adapter

                        binding.cvSpinnerBaseCurrency.show(true)
                        binding.cvSpinnerTargetCurrency.show(true)
                        binding.swap.show(true)

                        binding.cvSpinnerTargetCurrencyShimmer.stopShimmer()
                        binding.cvSpinnerBaseCurrencyShimmer.stopShimmer()
                        binding.cvSpinnerTargetCurrencyShimmer.show(false)
                        binding.cvSpinnerBaseCurrencyShimmer.show(false)

                        fragment.showSnackbar(
                            state.message, Snackbar.LENGTH_INDEFINITE,
                            "Retry",
                            retryGetSymbolsListener
                        )
                    }
                    is ConverterViewState.Error.ErrorGettingConversion -> {
                        binding.llMarketRate.show(false)
                        Toast.makeText(
                            context,
                            "Error converting currency. Try again later.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is ConverterViewState.Error.ErrorGettingChart -> {
                        binding.tvEmptyState.text = context.getString(R.string.error_getting_chart_data)
                        binding.tabLayout.show(false)
                        binding.tabViewpager.show(false)
                        binding.llMarketRate.show(false)
                        binding.tvEmptyState.show(true)

                        showConversionProgress(false)
                        enableConvertButton(true)
                    }
                }
            }
            ConverterViewState.GettingChartData -> {
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

                showConversionProgress(false)
                enableConvertButton(true)
            }
        }
    }

    private fun showCurrencyCalculator() {
        val spannable = SpannableString("Currency\nCalculator.")
        spannable.setSpan(
            ForegroundColorSpan(Color.GREEN),
            spannable.length - 1, spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.currencyConverter.text = spannable
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

    private val retryGetSymbolsIntent: Flow<ConverterViewIntent>
        get() = callbackFlow {
            val listener = OnClickListener { safeOffer(ConverterViewIntent.LoadSymbols) }
            retryGetSymbolsListener = listener
            awaitClose {
                retryGetSymbolsListener = null
            }
        }

    override val intents: Flow<ConverterViewIntent>
        get() = merge(getRateIntent, retryGetSymbolsIntent)
}