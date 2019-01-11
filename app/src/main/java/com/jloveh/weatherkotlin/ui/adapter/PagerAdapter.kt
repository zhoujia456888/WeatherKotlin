package com.jloveh.weatherkotlin.ui.adapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jloveh.weatherkotlin.database.Location
import com.jloveh.weatherkotlin.ui.fragment.WeatherFragment

class PagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var locations: MutableList<Location>
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment = WeatherFragment()
        var bundle = Bundle()
        bundle.putString("cid", locations.get(position).cid)
        bundle.putInt("index", position)
        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int {
        return locations.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return locations.get(position).location
    }


}