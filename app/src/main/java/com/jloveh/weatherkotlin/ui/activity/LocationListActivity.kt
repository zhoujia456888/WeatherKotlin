package com.jloveh.weatherkotlin.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.jloveh.weatherkotlin.R
import com.jloveh.weatherkotlin.base.BaseActivity
import com.jloveh.weatherkotlin.database.Location
import com.jloveh.weatherkotlin.database.LocationDBUtils
import com.jloveh.weatherkotlin.database.LocationDBUtils.Companion.deleteLocation
import com.jloveh.weatherkotlin.database.LocationDBUtils.Companion.updateSorting
import com.jloveh.weatherkotlin.ui.adapter.LocationListAdapter
import com.jloveh.weatherkotlin.utils.MessageEvent
import com.jloveh.weatherkotlin.utils.MessageEvent.Companion.data_change
import com.yanzhenjie.recyclerview.swipe.*
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMovementListener
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration
import kotlinx.android.synthetic.main.activity_locationlist.*
import kotlinx.android.synthetic.main.title_bar.*
import org.greenrobot.eventbus.EventBus
import java.util.*


class LocationListActivity : BaseActivity() {

    var locations: MutableList<Location>? = null

    var locationAdapter: LocationListAdapter? = null


    companion object {
        lateinit var activity: Activity
        var addLocationSucc = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_locationlist
        )

        activity = this

        txt_title.text = getString(R.string.city_list)
        btn_more2.visibility = View.VISIBLE
        btn_back.visibility = View.VISIBLE
        btn_back.setOnClickListener { finish() }
        btn_more2.setOnClickListener {
            (activity as LocationListActivity).startActivityForResult(
                Intent(
                    activity,
                    InputLocationActivity::class.java
                ), 25
            )
        }

        locations = LocationDBUtils.getAllLocation()!!
        var locationListLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        locationAdapter = LocationListAdapter(R.layout.item_location, locations)
        rv_location_list.layoutManager = locationListLayoutManager

        rv_location_list.addItemDecoration(DefaultItemDecoration(resources.getColor(R.color.color_text2)))
        rv_location_list.isLongPressDragEnabled = true // 拖拽排序，默认关闭。
        rv_location_list.setOnItemMoveListener(object : OnItemMoveListener {
            override fun onItemDismiss(p0: RecyclerView.ViewHolder?) {
            }

            override fun onItemMove(
                srcHolder: RecyclerView.ViewHolder?,
                targetHolder: RecyclerView.ViewHolder?
            ): Boolean {
                val fromPosition = srcHolder!!.adapterPosition
                val toPosition = targetHolder!!.adapterPosition

                LogUtils.e("从$fromPosition 到 $toPosition")

                Collections.swap(locations, fromPosition, toPosition)
                locationAdapter!!.notifyItemMoved(fromPosition, toPosition)

                locations!![fromPosition].sorting = toPosition.toLong()

                updateSorting(locations!![fromPosition])

                if (fromPosition > toPosition) {
                    locations!![toPosition].sorting = toPosition - 1.toLong()
                    updateSorting(locations!![toPosition])
                } else {
                    locations!![toPosition].sorting = toPosition + 1.toLong()
                    updateSorting(locations!![toPosition])
                }

                EventBus.getDefault().post(MessageEvent(data_change))

                return true
            }
        })


        val width =
            getResources().getDimensionPixelSize(R.dimen.abc_action_bar_content_inset_with_nav)
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        rv_location_list.setSwipeMenuCreator(object : SwipeMenuCreator {
            override fun onCreateMenu(leftMenu: SwipeMenu?, rightMenu: SwipeMenu?, position: Int) {
                var deleteItem: SwipeMenuItem = SwipeMenuItem(activity)
                    .setBackgroundColor(resources.getColor(R.color.red))
                    .setText(getString(R.string.delete))
                    .setTextColor(resources.getColor(R.color.white))
                    .setWidth(width)
                    .setHeight(height)

                rightMenu!!.addMenuItem(deleteItem) // 在Item左侧添加一个菜单。
            }
        })

        rv_location_list.setSwipeMenuItemClickListener(object : SwipeMenuItemClickListener {
            override fun onItemClick(menuBridge: SwipeMenuBridge?, position: Int) {
                menuBridge!!.closeMenu()

                MaterialDialog(activity!!).show {
                    cancelable(false)
                    cancelOnTouchOutside(false)
                    title(R.string.delete)
                    message(R.string.delete_city_hint)
                    positiveButton(R.string.confirm) { dialog ->
                        deleteLocation(locations!![position])
                        EventBus.getDefault().post(MessageEvent(data_change))
                        locations!!.removeAt(position)
                        locationAdapter!!.notifyDataSetChanged()
                    }
                }

            }
        })

        rv_location_list.adapter = locationAdapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            addLocationSucc -> activity.finish()
        }

    }
}