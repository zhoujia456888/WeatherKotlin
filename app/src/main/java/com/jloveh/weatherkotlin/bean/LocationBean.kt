package com.jloveh.weatherkotlin.bean

data class LocationBean(
    val HeWeather6: List<HeWeather>
) {
    data class HeWeather(
        val basic: MutableList<Basic>,
        val status: String // ok
    ) {
        data class Basic(
            val cid: String, // CN101250106
            val location: String, // 长沙县
            val parent_city: String, // 长沙
            val admin_area: String, // 湖南
            val cnty: String, // 中国
            val lat: String, // 28.23788834
            val lon: String, // 113.08010101
            val tz: String, // +8.00
            val type: String // city
        )
    }
}