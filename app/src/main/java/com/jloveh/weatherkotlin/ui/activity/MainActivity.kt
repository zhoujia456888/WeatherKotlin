package com.jloveh.weatherkotlin.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.MaterialDialog
import com.jloveh.weatherkotlin.R
import com.jloveh.weatherkotlin.base.BaseActivity
import com.jloveh.weatherkotlin.database.Location
import com.jloveh.weatherkotlin.database.LocationDBUtils.Companion.getAllLocation
import com.jloveh.weatherkotlin.ui.adapter.PagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.title_bar.*

class MainActivity : BaseActivity() {


    var text: String? = null

    var activity: Activity? = null

    var locations: MutableList<Location>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity = this


        btn_more2.visibility = View.VISIBLE

        var locationIntent: Intent = Intent(activity, LocationListActivity::class.java)

        btn_more2.setOnClickListener {
            (activity as MainActivity).startActivity(locationIntent)
        }

        locations = getAllLocation()!!

        if (locations!!.size == 0) {
            MaterialDialog(this).show {
                cancelable(false)
                cancelOnTouchOutside(false)
                title(R.string.no_location)
                message(R.string.no_location_context)
                positiveButton(R.string.confirm) { dialog ->
                    (activity as MainActivity).startActivity(locationIntent)
                }
            }

            txt_title.visibility = View.GONE
            viewpage_weather.visibility = View.GONE
            ll_nothing.visibility = View.VISIBLE

        } else {
            viewpage_weather.visibility = View.VISIBLE
            ll_nothing.visibility = View.GONE

            val pagerAdapter = PagerAdapter(this, supportFragmentManager, locations!!)
            viewpage_weather.adapter = pagerAdapter

            txt_title.text = locations!!.get(0).location
        }

        viewpage_weather.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                txt_title.text = locations!!.get(position).location
            }

        })

    }

}
