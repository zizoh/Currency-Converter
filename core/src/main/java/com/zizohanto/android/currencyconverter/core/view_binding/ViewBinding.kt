package com.zizohanto.android.currencyconverter.core.view_binding

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewBindingDelegate<T : ViewBinding>(
    private val fragment: Fragment,
    private val viewBindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

    init {
        fragment.lifecycle.addObserver(this)
    }

    private var _value: T? = null

    private val viewLifecycleObserver: DefaultLifecycleObserver =
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                disposeValue()
            }
        }

    private fun disposeValue() {
        _value = null
    }

    override fun onCreate(owner: LifecycleOwner) {
        fragment.viewLifecycleOwnerLiveData.observe(fragment, Observer<LifecycleOwner> {
            it?.lifecycle?.addObserver(viewLifecycleObserver)
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding: T? = _value
        if (binding != null) {
            return binding
        }

        val lifecycle: Lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException(
                "Should not attempt to get bindings when " +
                        "Fragment views are destroyed."
            )
        }

        return viewBindingFactory(thisRef.requireView()).also {
            _value = it
        }
    }
}

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T): ViewBindingDelegate<T> =
    ViewBindingDelegate(
        fragment = this,
        viewBindingFactory = viewBindingFactory
    )
