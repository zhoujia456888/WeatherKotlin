package com.jloveh.weatherkotlin.ui.adapter

import android.view.View
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jloveh.weatherkotlin.R.id.*
import com.jloveh.weatherkotlin.bean.LocationBean.HeWeather.Basic

class LocationAdapter(layoutResId: Int, data: MutableList<Basic>?) :
    BaseQuickAdapter<Basic, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: Basic?) {

        helper!!.setText(txt_location, item!!.location + ",")

        helper.setGone(txt_parent_city, !item.parent_city.isNullOrEmpty())
        helper.setText(txt_parent_city, item.parent_city + ",")

        helper.setGone(txt_admin_area, !item.admin_area.isNullOrEmpty())
        helper.setText(txt_admin_area, item.admin_area + ",")

        helper.setGone(txt_cnty, !item.cnty.isNullOrEmpty())
        helper.setText(txt_cnty, item.cnty)

    }


}