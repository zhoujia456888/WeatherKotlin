package com.jloveh.weatherkotlin.ui.adapter

import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jloveh.weatherkotlin.R
import com.jloveh.weatherkotlin.bean.WeatherBean

class LifestyleAdapter(
    layoutResId: Int,
    data: MutableList<WeatherBean.HeWeather.Lifestyle>?
) :
    BaseQuickAdapter<WeatherBean.HeWeather.Lifestyle, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: WeatherBean.HeWeather.Lifestyle?) {

        var type: String? = null
        when (item!!.type) {
            "comf" -> type = mContext.getString(R.string.comf)
            "cw" -> type = mContext.getString(R.string.cw)
            "drsg" -> type = mContext.getString(R.string.drsg)
            "flu" -> type = mContext.getString(R.string.flu)
            "sport" -> type = mContext.getString(R.string.sport)
            "trav" -> type = mContext.getString(R.string.trav)
            "uv" -> type = mContext.getString(R.string.uv)
            "air" -> type = mContext.getString(R.string.air)
            "ac" -> type = mContext.getString(R.string.ac)
            "ag" -> type = mContext.getString(R.string.ag)
            "gl" -> type = mContext.getString(R.string.gl)
            "mu" -> type = mContext.getString(R.string.mu)
            "airc" -> type = mContext.getString(R.string.airc)
            "ptfc" -> type = mContext.getString(R.string.ptfc)
            "fsh" -> type = mContext.getString(R.string.fsh)
            "spi" -> type = mContext.getString(R.string.spi)
        }


        helper!!.setText(R.id.txt_lifestyle_type, type)

        helper.setText(R.id.txt_lifestyle_brf, item.brf)

        helper.setText(R.id.txt_lifestyle_txt, item.txt)

    }
}