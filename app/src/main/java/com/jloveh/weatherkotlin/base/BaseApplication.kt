package com.jloveh.weatherkotlin.base

import android.app.Activity
import android.app.Application
import android.location.Location
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.jloveh.weatherkotlin.BuildConfig
import com.jloveh.weatherkotlin.database.MyObjectBox
import com.jloveh.weatherkotlin.retrofit.ApiClient
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import javax.inject.Inject

class BaseApplication : Application() {

    @Inject
    lateinit var mActivityList: MutableList<Activity?>


    companion object {
        lateinit var INSTANCE: BaseApplication
            private set

        lateinit var boxStore: BoxStore

        fun getBoxStoreInstance() = boxStore

    }


    override fun onCreate() {
        super.onCreate()

        Utils.init(this)//初始化AndroidUtils

        init()

        ApiClient.instance.init()

        initObjectBox()


    }

    private fun initObjectBox() {
        boxStore = MyObjectBox.builder().androidContext(this).build()
        if (BuildConfig.DEBUG) {
            boxStore?.let {
                //可以理解为初始化连接浏览器(可以在浏览器中查看数据，下面再说)
                val started = AndroidObjectBrowser(boxStore).start(this)
                LogUtils.i("ObjectBrowser", "Started: " + started)
            }
        }
    }

    private fun init() {
        INSTANCE = this
    }

    fun quitApp() {
        for (activity in mActivityList) {
            activity?.finish()
        }
    }


}