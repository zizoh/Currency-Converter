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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

@AndroidEntryPoint
class ConverterFragment : Fragment(R.layout.fragment_converter),
    MVIView<ConverterViewIntent, ConverterViewState> {

    private val viewModel: ConverterViewModel by viewModels()

    private val binding: FragmentConverterBinding by viewBinding(FragmentConverterBinding::bind)

    override val intents: Flow<ConverterViewIntent>
        get() = merge(binding.converter.intents)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.converter.fragment = this
        viewModel.processIntent(intents)
        viewModel.viewState.observe(viewLifecycleOwner, ::render)
    }

    override fun render(state: ConverterViewState) {
        binding.converter.render(state)
    }
}