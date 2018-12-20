package com.jloveh.weatherkotlin.bean

data class WeatherBean(
    val HeWeather6: List<HeWeather>
) {
    data class HeWeather(
        val basic: Basic,
        val update: Update,
        val status: String, // ok
        val now: Now,
        val daily_forecast: MutableList<DailyForecast>,
        val hourly: MutableList<Hourly>,
        val lifestyle: MutableList<Lifestyle>
    ) {
        data class Basic(
            val cid: String, // CN101010100
            val location: String, // 北京
            val parent_city: String, // 北京
            val admin_area: String, // 北京
            val cnty: String, // 中国
            val lat: String, // 39.90498734
            val lon: String, // 116.4052887
            val tz: String // +8.00
        )

        data class Hourly(
            val cloud: String, // 0
            val cond_code: String, // 100
            val cond_txt: String, // 晴
            val dew: String, // -26
            val hum: String, // 49
            val pop: String, // 0
            val pres: String, // 1029
            val time: String, // 2018-12-12 16:00
            val tmp: String, // 1
            val wind_deg: String, // 196
            val wind_dir: String, // 西南风
            val wind_sc: String, // 1-2
            val wind_spd: String // 1
        )

        data class DailyForecast(
            val cond_code_d: String, // 100
            val cond_code_n: String, // 100
            val cond_txt_d: String, // 晴
            val cond_txt_n: String, // 晴
            val date: String, // 2018-12-17
            val hum: String, // 28
            val mr: String, // 13:30
            val ms: String, // 01:07
            val pcpn: String, // 0.0
            val pop: String, // 0
            val pres: String, // 1026
            val sr: String, // 07:30
            val ss: String, // 16:51
            val tmp_max: String, // 4
            val tmp_min: String, // -4
            val uv_index: String, // 2
            val vis: String, // 20
            val wind_deg: String, // 251
            val wind_dir: String, // 西南风
            val wind_sc: String, // 1-2
            val wind_spd: String // 6
        )

        data class Lifestyle(
            val type: String, // air
            val brf: String, // 良
            val txt: String // 气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。
        )

        data class Update(
            val loc: String, // 2018-12-11 16:45
            val utc: String // 2018-12-11 08:45
        )

        data class Now(
            val cloud: String, // 0
            val cond_code: String, // 100
            val cond_txt: String, // 晴
            val fl: String, // -6
            val hum: String, // 13
            val pcpn: String, // 0.0
            val pres: String, // 1036
            val tmp: String, // -2
            val vis: String, // 10
            val wind_deg: String, // 6
            val wind_dir: String, // 北风
            val wind_sc: String, // 2
            val wind_spd: String // 6
        )
    }
}