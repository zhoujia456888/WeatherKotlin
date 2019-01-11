package com.jloveh.weatherkotlin.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jloveh.weatherkotlin.R
import com.jloveh.weatherkotlin.base.BaseActivity
import com.jloveh.weatherkotlin.bean.LocationBean
import com.jloveh.weatherkotlin.bean.LocationBean.HeWeather.Basic
import com.jloveh.weatherkotlin.constant.Constant
import com.jloveh.weatherkotlin.database.LocationDBUtils.Companion.insertLocation
import com.jloveh.weatherkotlin.retrofit.ApiClient
import com.jloveh.weatherkotlin.retrofit.ApiResponse
import com.jloveh.weatherkotlin.retrofit.NetworkScheduler
import com.jloveh.weatherkotlin.ui.activity.LocationListActivity.Companion.addLocationSucc
import com.jloveh.weatherkotlin.ui.adapter.LocationAdapter
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.kotlin.bindUntilEvent
import com.zyr.apiclient.network.ApiErrorModel
import kotlinx.android.synthetic.main.activity_inputlocation.*
import kotlinx.android.synthetic.main.bar_input_location.*
import kotlinx.android.synthetic.main.item_location.*
import org.greenrobot.eventbus.EventBus
import com.jloveh.weatherkotlin.utils.MessageEvent
import com.jloveh.weatherkotlin.utils.MessageEvent.Companion.data_change


class InputLocationActivity : BaseActivity() {

    var activity: Activity? = null

    var locationAdapter: LocationAdapter? = null

    var locations: MutableList<Basic>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_inputlocation)

        activity = this

        btn_back.setOnClickListener { finish() }

        btn_clean_edtxt.setOnClickListener { edtxt_input_location.text = null }

        edtxt_input_location.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var location: String = s.toString()

                if (!StringUtils.isEmpty(location)) {
                    if (RegexUtils.isZh(location)) {
                        getLocation(location)
                    }
                } else {
                    locations!!.clear()
                    locationAdapter!!.setNewData(null)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        var layoutManager = LinearLayoutManager(activity)

        locationAdapter = LocationAdapter(R.layout.item_location, locations)
        rl_input_lication.layoutManager = layoutManager
        rl_input_lication.adapter = locationAdapter
        rl_input_lication.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        locationAdapter!!.onItemClickListener =
                BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                    LogUtils.e(locations!!.get(position))

                    var location = locations!!.get(position)

                    insertLocation(location)
                    EventBus.getDefault().post(MessageEvent(data_change))

                    setResult(addLocationSucc, Intent())

                    finish()

                }
    }

    private fun getLocation(location: String) {
        ApiClient.instance.locationService.getLocation(location, Constant.key)
            .compose(NetworkScheduler.compose())
            .bindUntilEvent(this, ActivityEvent.DESTROY)
            .subscribe(object : ApiResponse<LocationBean>(this) {
                override fun success(data: LocationBean) {
                    var heweather = data.HeWeather6[0]
                    var status = heweather.status
                    if (status.equals("ok")) {
                        locations = heweather.basic
                        locationAdapter!!.setNewData(locations!!)
                    } else {
                        ToastUtils.showShort(data.HeWeather6[0].status)
                    }
                }

                override fun failure(statusCode: Int, apiErrorModel: ApiErrorModel) {
                    ToastUtils.showShort(apiErrorModel.message)
                }
            })
    }
}