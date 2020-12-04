package com.zizohanto.android.currencyconverter.converter.ui.converter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zizohanto.android.currencyconverter.converter.presentation.views.CustomPager


class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    var tabs: MutableList<Fragment> = mutableListOf()
    var titles: MutableList<String>? = mutableListOf()
    private var mCurrentPosition = -1

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun getItem(position: Int): Fragment {
        return tabs[position]
    }

    override fun getCount(): Int {
        return tabs.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when {
            titles != null -> titles!![position]
            else -> "PAGE_$position"
        }
    }


    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (position != mCurrentPosition) {
            val fragment = `object` as Fragment?
            val pager = container as CustomPager
            if (fragment != null && fragment.view != null) {
                mCurrentPosition = position
                pager.measureCurrentView(fragment.view)
            }
        }
    }

    fun addFrag(fragment: Fragment, title: String) {
        tabs.add(fragment)
        titles?.add(title)
    }

}
