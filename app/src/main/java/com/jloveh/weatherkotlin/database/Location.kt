package com.jloveh.weatherkotlin.database

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


@Entity
data class Location(
    @Id
    var id: Long = 0,

    var cid: String,// CN101010100       地区／城市ID
    var location: String, // 北京          地区／城市名称
    var parent_city: String, // 北京       该地区／城市的上级城市
    var admin_area: String, // 北京        该地区／城市所属行政区域
    var cnty: String, // 中国              该地区／城市所属国家名称
    var lat: String, // 39.90498734        地区／城市纬度
    var lon: String, // 116.4052887        地区／城市经度
    var tz: String, // +8.00               该地区／城市所在时区
    var type: String, // city              该地区／城市的属性，目前有city城市和scenic中国景点
    
    var sorting: Long //                    排序

)