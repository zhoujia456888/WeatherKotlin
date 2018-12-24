package com.jloveh.weatherkotlin.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import com.jloveh.weatherkotlin.R
import com.jloveh.weatherkotlin.base.BaseActivity
import com.jloveh.weatherkotlin.database.Location
import com.jloveh.weatherkotlin.database.LocationDBUtils
import com.jloveh.weatherkotlin.database.LocationDBUtils.Companion.updateSorting
import com.jloveh.weatherkotlin.ui.adapter.LocationListAdapter
import kotlinx.android.synthetic.main.activity_inputlocation.*
import kotlinx.android.synthetic.main.activity_locationlist.*
import kotlinx.android.synthetic.main.title_bar.*

class LocationListActivity : BaseActivity() {

    var locations: MutableList<Location>? = null

    var locationAdapter: LocationListAdapter? = null
    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mItemDragAndSwipeCallback: ItemDragAndSwipeCallback

    var activity: Activity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_locationlist
        )

        activity = this

        btn_more2.visibility = View.VISIBLE
        btn_back.visibility = View.VISIBLE
        btn_back.setOnClickListener { finish() }
        var locationIntent: Intent = Intent(activity, InputLocationActivity::class.java)
        btn_more2.setOnClickListener {
            (activity as LocationListActivity).startActivity(locationIntent)
        }


        locations = LocationDBUtils.getAllLocation()!!
        var locationListLayoutManager = LinearLayoutManager(this)
        locationAdapter = LocationListAdapter(R.layout.item_location, locations)
        rv_location_list.layoutManager = locationListLayoutManager

        mItemDragAndSwipeCallback = ItemDragAndSwipeCallback(locationAdapter)
        mItemTouchHelper = ItemTouchHelper(mItemDragAndSwipeCallback)
        mItemTouchHelper.attachToRecyclerView(rv_location_list)

        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START or ItemTouchHelper.END)
        locationAdapter!!.enableSwipeItem()
        locationAdapter!!.enableDragItem(mItemTouchHelper)

        locationAdapter!!.setOnItemDragListener(object : OnItemDragListener {
            override fun onItemDragMoving(
                source: RecyclerView.ViewHolder?,
                from: Int,
                target: RecyclerView.ViewHolder?,
                to: Int
            ) {

            }

            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {

            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                LogUtils.e(pos)
            }

        })

        locationAdapter!!.setOnItemSwipeListener(object : OnItemSwipeListener {
            override fun clearView(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                LogUtils.e("View reset: " + pos)
            }

            override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                LogUtils.e("View Swiped: " + pos)
            }

            override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                LogUtils.e("view swiped start: " + pos)
            }

            override fun onItemSwipeMoving(
                canvas: Canvas?,
                viewHolder: RecyclerView.ViewHolder?,
                dX: Float,
                dY: Float,
                isCurrentlyActive: Boolean
            ) {
                canvas!!.drawColor(
                    ContextCompat.getColor(
                        this@LocationListActivity,
                        R.color.red
                    )
                )
            }
        })



        rv_location_list.adapter = locationAdapter
        rv_location_list.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )


    }
}