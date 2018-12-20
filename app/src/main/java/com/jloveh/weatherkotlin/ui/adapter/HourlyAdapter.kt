package com.jloveh.weatherkotlin.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jloveh.weatherkotlin.R
import com.jloveh.weatherkotlin.R.id.*
import com.jloveh.weatherkotlin.bean.WeatherBean
import com.jloveh.weatherkotlin.constant.Constant
import com.jloveh.weatherkotlin.utils.GlideApp

class HourlyAdapter(
    layoutResId: Int,
    data: MutableList<WeatherBean.HeWeather.Hourly>?
) :
    BaseQuickAdapter<WeatherBean.HeWeather.Hourly, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: WeatherBean.HeWeather.Hourly?) {

        helper!!.setText(txt_hourly, item!!.time.substringAfter(" ", "00"))

        helper!!.setText(txt_hourly_weather, item!!.cond_txt)

        var weatherimg = Constant.weatherImg_url + item.cond_code + ".png"  //天气图标
        GlideApp.with(mContext).load(weatherimg).placeholder(R.mipmap.cond_icon_100)
            .into(helper.getView(R.id.img_hourly))

        helper.setText(txt_hourly_temp, item.tmp + "°")


    }
}