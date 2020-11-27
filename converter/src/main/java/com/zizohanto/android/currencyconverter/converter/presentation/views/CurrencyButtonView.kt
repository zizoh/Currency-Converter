package com.zizohanto.android.currencyconverter.converter.presentation.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.zizohanto.android.currencyconverter.converter.databinding.CurrencyButtonLayoutBinding
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.debounce
import reactivecircus.flowbinding.common.safeOffer

/**
 * Created by zizoh on 26/November/2020.
 */

typealias ActionButtonClickListener = () -> Unit

class CurrencyButtonView : LinearLayout {

    private var binding: CurrencyButtonLayoutBinding

    private var buttonClickListener: ActionButtonClickListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = CurrencyButtonLayoutBinding.inflate(inflater, this, true)
    }

    fun setCurrencyFlag(emptyStateImageSrc: Drawable?) {
        if (emptyStateImageSrc != null) {
            binding.currencyFlag.setImageDrawable(emptyStateImageSrc)
        }
    }

    fun setCurrencySymbol(emptyStateCaption: String?) {
        if (emptyStateCaption != null) {
            binding.currencySymbol.text = emptyStateCaption
        }
    }

    fun getCurrencySymbol(): String = binding.currencySymbol.text.toString()

    val clicks: Flow<Unit>
        get() = callbackFlow {
            val listener: () -> Unit = {
                safeOffer(Unit)
                Unit
            }
            buttonClickListener = listener
            awaitClose { buttonClickListener = null }
        }.conflate().debounce(200)
}