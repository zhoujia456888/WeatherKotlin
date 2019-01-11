package com.jloveh.weatherkotlin.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.LogUtils
import com.jloveh.weatherkotlin.R
import com.jloveh.weatherkotlin.base.BaseActivity
import com.jloveh.weatherkotlin.database.Location
import com.jloveh.weatherkotlin.database.LocationDBUtils.Companion.getAllLocation
import com.jloveh.weatherkotlin.ui.adapter.PagerAdapter
import com.jloveh.weatherkotlin.utils.MessageEvent
import com.jloveh.weatherkotlin.utils.MessageEvent.Companion.data_change
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.title_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {


    var text: String? = null

    var activity: Activity? = null

    var locations: MutableList<Location>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity = this

        //注册EventBus
        EventBus.getDefault().register(this)

        btn_more2.visibility = View.VISIBLE

        var locationIntent: Intent = Intent(activity, LocationListActivity::class.java)

        btn_more2.setOnClickListener {
            (activity as MainActivity).startActivity(locationIntent)
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

        initData()

    }

    fun initData() {
        locations = getAllLocation()!!

        if (locations!!.size == 0) {
            MaterialDialog(this).show {
                cancelable(false)
                cancelOnTouchOutside(false)
                title(R.string.no_location)
                message(R.string.no_location_context)
                positiveButton(R.string.confirm) { dialog ->
                    var inputLocationIntent: Intent =
                        Intent(activity, InputLocationActivity::class.java)
                    (activity as MainActivity).startActivity(inputLocationIntent)
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

            txt_title.visibility = View.VISIBLE
            txt_title.text = locations!!.get(0).location
        }
    }

    //EventBus获取数据变化坚挺
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        LogUtils.e("接收到EventBus")
        when (event.type) {
            data_change -> initData()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
