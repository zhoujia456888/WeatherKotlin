package com.jloveh.weatherkotlin.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.jloveh.weatherkotlin.R
import com.jloveh.weatherkotlin.bean.WeatherBean
import com.jloveh.weatherkotlin.constant.Constant
import com.jloveh.weatherkotlin.constant.Constant.weatherImg_url
import com.jloveh.weatherkotlin.retrofit.ApiClient
import com.jloveh.weatherkotlin.retrofit.ApiResponse
import com.jloveh.weatherkotlin.retrofit.NetworkScheduler
import com.jloveh.weatherkotlin.ui.adapter.DailyForecastAdapter
import com.jloveh.weatherkotlin.ui.adapter.HourlyAdapter
import com.jloveh.weatherkotlin.ui.adapter.LifestyleAdapter
import com.jloveh.weatherkotlin.ui.adapter.LocationAdapter
import com.jloveh.weatherkotlin.utils.GlideApp
import com.jloveh.weatherkotlin.utils.MyGlideApp
import com.sctdroid.app.uikit.CurveView
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.components.support.RxFragment
import com.trello.rxlifecycle3.kotlin.bindUntilEvent
import com.zyr.apiclient.network.ApiErrorModel
import kotlinx.android.synthetic.main.activity_inputlocation.*
import kotlinx.android.synthetic.main.fragment_weather.*
import java.text.SimpleDateFormat

class WeatherFragment : RxFragment(), SwipeRefreshLayout.OnRefreshListener {

    var cid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.fragment_weather, container, false)
        var bundel = arguments
        cid = bundel!!.getString("cid")
        var index = bundel!!.getInt("index")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sl_weather.setOnRefreshListener(this)
        getWeather(cid)
    }

    override fun onRefresh() {
        Handler().postDelayed({
            getWeather(cid)
            sl_weather.isRefreshing = false
        }, 2000)
    }

    //获取天气数据
    private fun getWeather(cid: String?) {
        ApiClient.instance.service.getForecast(cid!!, Constant.key)
            .compose(NetworkScheduler.compose())
            .bindUntilEvent(this!!, FragmentEvent.DESTROY)
            .subscribe(object : ApiResponse<WeatherBean>(context!!) {
                override fun success(data: WeatherBean) {
                    var heWeather = data.HeWeather6[0]
                    var status = heWeather.status
                    if (status.equals("ok")) {
                        updateUI(heWeather)
                    } else {
                        ToastUtils.showShort(data.HeWeather6[0].status)
                    }
                }

                override fun failure(statusCode: Int, apiErrorModel: ApiErrorModel) {
                    ToastUtils.showShort(apiErrorModel.message)
                }
            })
    }

    private fun updateUI(heWeather: WeatherBean.HeWeather) {
        var update = heWeather.update
        var now: WeatherBean.HeWeather.Now = heWeather.now
        var dailyForecasts: MutableList<WeatherBean.HeWeather.DailyForecast> =
            heWeather.daily_forecast
        var hourly: MutableList<WeatherBean.HeWeather.Hourly> = heWeather.hourly
        var lifestyle: MutableList<WeatherBean.HeWeather.Lifestyle> = heWeather.lifestyle

        //实时天气
        var weatherimg = weatherImg_url + now.cond_code + ".png"  //天气图标
        GlideApp.with(this).load(weatherimg).placeholder(R.mipmap.cond_icon_100)
            .into(img_weather_now)
        txt_temp_new.text = now.tmp + getString(R.string.du)        //温度
        txt_weather_now.text = now.cond_txt       //天气
        txt_wind_now.text = now.wind_dir + "  " + now.wind_sc + getString(R.string.ji)       //风向，风力
        txt_update.text = getString(R.string.update_yu) + update.loc       //更新时间

        txt_fl_temp.text = now.fl + getString(R.string.du2)     //体感温度
        txt_hum.text = now.hum + getString(R.string.baifen)  //相对湿度
        txt_wind_sc.text = now.wind_sc + getString(R.string.ji)       //风力
        wind_dir.text = now.wind_dir    //风向
        txt_wind_spd.text = now.wind_spd + getString(R.string.km_h)  //风速
        txt_pcpn.text = now.pcpn         //降水量
        txt_pres.text = now.pres + getString(R.string.baipa)      //气压
        txt_vis.text = now.vis + getString(R.string.gongli)      //能见度
        txt_cloud.text = now.cloud    //云量

        //天气预报
        var dailyForecastLayoutManager = GridLayoutManager(activity, dailyForecasts.size)
        var dailyForecastAdapter: DailyForecastAdapter =
            DailyForecastAdapter(R.layout.item_daily_forecast, dailyForecasts)
        rv_daily_forecast.layoutManager = dailyForecastLayoutManager
        rv_daily_forecast.isNestedScrollingEnabled=false
        rv_daily_forecast.adapter = dailyForecastAdapter

        setDailyForecastAdapter(dailyForecasts)  //温度变化曲线图

        var hourlyLayoutManager = GridLayoutManager(activity, hourly.size)
        var hourlyAdapter: HourlyAdapter =
            HourlyAdapter(R.layout.item_hourly, hourly)
        rv_hourly.isNestedScrollingEnabled=false
        rv_hourly.layoutManager = hourlyLayoutManager
        rv_hourly.adapter = hourlyAdapter

        //生活指数
        var lifestyleLayoutManager = LinearLayoutManager(activity)
        var lifestyleAdapter: LifestyleAdapter =
            LifestyleAdapter(R.layout.item_lifestyle, lifestyle)
        rv_lifestyle.isNestedScrollingEnabled=false
        rv_lifestyle.layoutManager = lifestyleLayoutManager
        rv_lifestyle.adapter = lifestyleAdapter

    }


    private fun setDailyForecastAdapter(dailyForecasts: MutableList<WeatherBean.HeWeather.DailyForecast>) {
        var hourlyWithMaxtmp = dailyForecasts.maxBy { it -> it.tmp_max.toInt() }//依据温度获取最最高温度
        var hourlyWithMintmp = dailyForecasts.minBy { it -> it.tmp_min.toInt() }//依据温度获取最最低温度
        var maxtmp = hourlyWithMaxtmp!!.tmp_max.toInt()
        var mintmp = hourlyWithMintmp!!.tmp_min.toInt()

        var curveViewAdapter = object : CurveView.Adapter() {

            override fun getLevel(position: Int): Int {
                return (dailyForecasts.get(position).tmp_max.toInt() + dailyForecasts.get(position).tmp_min.toInt()) / 2
            }

            override fun getCount(): Int {
                return dailyForecasts.size
            }

            /**
             * @return y 轴下限
             */
            override fun getMinLevel(): Int {
                return mintmp
            }

            /**
             * @return y 轴上限
             */
            override fun getMaxLevel(): Int {
                return maxtmp
            }

            /**
             * 设置点上的文字，每个mark是一个，可同时设置点的 8 个方向的文字
             * 注意: Gravity 应使用 CurveView.Gravity 类
             * @param position
             * @return
             */
            override fun onCreateMarks(position: Int): Set<CurveView.Mark> {
                val marks = HashSet<CurveView.Mark>()
                val mark = CurveView.Mark(
                    dailyForecasts.get(position).tmp_max + "°",
                    CurveView.Gravity.START or CurveView.Gravity.CENTER_HORIZONTAL,
                    0,
                    0,
                    0,
                    20
                )
                val mark1 = CurveView.Mark(
                    dailyForecasts.get(position).tmp_min + "°",
                    CurveView.Gravity.BOTTOM or CurveView.Gravity.CENTER_HORIZONTAL,
                    0,
                    20,
                    0,
                    0
                )
                marks.add(mark)
                marks.add(mark1)
                return marks
            }

        }

        curve_view_hourly.setAdapter(curveViewAdapter)
        curveViewAdapter.notifyDataSetChanged()
    }
}