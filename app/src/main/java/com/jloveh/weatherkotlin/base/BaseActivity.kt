package com.jloveh.weatherkotlin.base

import android.os.Bundle
import com.blankj.utilcode.util.BarUtils
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

open class BaseActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BarUtils.setStatusBarLightMode(this, true)//设置状态栏为浅色模式
    }
}