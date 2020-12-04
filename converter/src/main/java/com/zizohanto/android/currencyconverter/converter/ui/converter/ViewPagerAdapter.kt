package com.zizohanto.android.currencyconverter.converter.ui.converter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    private val tabs: List<Fragment>, fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return tabs[position]
    }

    override fun getItemCount(): Int {
        return tabs.size
    }

}
