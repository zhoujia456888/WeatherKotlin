package com.jloveh.weatherkotlin.ui.adapter

import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jloveh.weatherkotlin.R
import com.jloveh.weatherkotlin.R.id.*
import com.jloveh.weatherkotlin.bean.WeatherBean
import com.jloveh.weatherkotlin.constant.Constant
import com.jloveh.weatherkotlin.utils.GlideApp
import java.text.SimpleDateFormat

class DailyForecastAdapter(
    layoutResId: Int,
    data: MutableList<WeatherBean.HeWeather.DailyForecast>?
) :
    BaseQuickAdapter<WeatherBean.HeWeather.DailyForecast, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: WeatherBean.HeWeather.DailyForecast?) {


        var daily: String? = null
        if (TimeUtils.isToday(item!!.date, SimpleDateFormat("yyyy-MM-dd"))) {
            daily = mContext.getString(R.string.today)
        } else {
            daily = TimeUtils.getChineseWeek(item!!.date, SimpleDateFormat("yyyy-MM-dd"))

        }
        helper!!.setText(txt_forecast_daily, daily)
        var weatherimg = Constant.weatherImg_url + item.cond_code_d + ".png"  //天气图标
        GlideApp.with(mContext).load(weatherimg).placeholder(R.mipmap.cond_icon_100)
            .into(helper.getView(img_forecast_daily))

        helper.setText(txt_forecast_weather, item.cond_txt_d)

        helper.setText(txt_forecast_temp, item.tmp_min + "/" + item.tmp_max)

    }
}