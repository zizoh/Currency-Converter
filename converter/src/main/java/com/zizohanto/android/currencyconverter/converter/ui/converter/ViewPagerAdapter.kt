package com.zizohanto.android.currencyconverter.converter.ui.converter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(parentFragment: Fragment) : FragmentStateAdapter(parentFragment) {

    private val fragments: MutableList<Fragment> = mutableListOf()

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    fun addFragment(numberOfEntries: Int, fragment: Fragment) {
        if (numberOfEntries == 30) {
            fragments.add(0, fragment)
        } else {
            fragments.add(fragment)
        }
    }

    fun clearFragments() {
        fragments.clear()
    }

}
