package com.zizohanto.android.currencyconverter.converter.ui.converter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zizohanto.android.currencyconverter.converter.R
import com.zizohanto.android.currencyconverter.converter.databinding.FragmentConverterBinding
import com.zizohanto.android.currencyconverter.converter.presentation.ConverterViewModel
import com.zizohanto.android.currencyconverter.converter.presentation.mvi.ConverterViewIntent
import com.zizohanto.android.currencyconverter.converter.presentation.mvi.ConverterViewState
import com.zizohanto.android.currencyconverter.core.ext.observe
import com.zizohanto.android.currencyconverter.core.view_binding.viewBinding
import com.zizohanto.android.currencyconverter.presentation.mvi.MVIView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@AndroidEntryPoint
class ConverterFragment : Fragment(R.layout.fragment_converter),
    MVIView<ConverterViewIntent, ConverterViewState> {

    private val viewModel: ConverterViewModel by viewModels()

    private val binding: FragmentConverterBinding by viewBinding(FragmentConverterBinding::bind)

    private val loadSymbols = ConflatedBroadcastChannel<ConverterViewIntent.LoadSymbols>()

    override val intents: Flow<ConverterViewIntent>
        get() = loadSymbols.asFlow()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.processIntent(intents)
        viewModel.viewState.observe(viewLifecycleOwner, ::render)
    }

    override fun onResume() {
        super.onResume()

        loadSymbols.offer(ConverterViewIntent.LoadSymbols)
    }

    override fun render(state: ConverterViewState) {
        binding.converter.render(state)
    }
}