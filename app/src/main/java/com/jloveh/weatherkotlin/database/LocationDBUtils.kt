package com.jloveh.weatherkotlin.database

import com.jloveh.weatherkotlin.base.BaseApplication
import com.jloveh.weatherkotlin.bean.LocationBean
import com.jloveh.weatherkotlin.database.Location_.location
import com.jloveh.weatherkotlin.database.Location_.sorting
import com.jloveh.weatherkotlin.utils.MessageEvent
import com.jloveh.weatherkotlin.utils.MessageEvent.Companion.data_change
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import org.greenrobot.eventbus.EventBus
import java.util.*

class LocationDBUtils {
    companion object {
        var boxStore = BaseApplication.getBoxStoreInstance()

        var locationBox: Box<Location> = boxStore.boxFor<Location>()


        fun getAllLocation(): MutableList<Location>? {
            return locationBox.query().order(sorting).build().find()
        }

        fun insertLocation(location: LocationBean.HeWeather.Basic) {

            var cid: String = location.cid
            var loca: String = location.location
            var parent_city: String = location.parent_city
            var admin_area: String = location.admin_area
            var cnty: String = location.cnty
            var lat: String = location.lat
            var lon: String = location.lon
            var tz: String = location.tz
            var type: String = location.type


            var location: Location = Location(
                cid = cid,
                location = loca,
                parent_city = parent_city,
                admin_area = admin_area,
                cnty = cnty,
                lat = lat,
                lon = lon,
                tz = tz,
                type = type,
                sorting = Date().time
            )

            locationBox.put(location)
        }

        fun updateSorting(location: Location) {
            locationBox.put(location)

        }


        fun deleteLocation(location: Location) {
            locationBox.remove(location)
        }
    }

}