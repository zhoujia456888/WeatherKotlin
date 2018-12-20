package com.jloveh.weatherkotlin.ui.adapter

import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jloveh.weatherkotlin.R
import com.jloveh.weatherkotlin.database.Location

class LocationListAdapter  (layoutResId: Int, data: MutableList<Location>?) :
    BaseItemDraggableAdapter<Location, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: Location) {

        helper!!.setText(R.id.txt_location, item!!.location + ",")

        helper.setGone(R.id.txt_parent_city, !item.parent_city.isNullOrEmpty())
        helper.setText(R.id.txt_parent_city, item.parent_city + ",")

        helper.setGone(R.id.txt_admin_area, !item.admin_area.isNullOrEmpty())
        helper.setText(R.id.txt_admin_area, item.admin_area + ",")

        helper.setGone(R.id.txt_cnty, !item.cnty.isNullOrEmpty())
        helper.setText(R.id.txt_cnty, item.cnty)

    }
}