package com.delishstudio.delish.system

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.maps.model.LatLng

class LocationUtils {

    companion object {

        fun distanceTo(lat_a: Double, lng_a: Double, lat_b: Double, lng_b: Double): Double {
            val locationA = Location("A")
            locationA.setLatitude(lat_a)
            locationA.setLongitude(lng_a)
            val locationB = Location("B")
            locationB.setLatitude(lat_b)
            locationB.setLongitude(lng_b)
            return locationA.distanceTo(locationB).toDouble()
        }

        fun getAddress(context: Context, latLng: LatLng): Address? {
            val geocoder = Geocoder(context)
            try {
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses != null && !addresses.isEmpty()) {
                    return addresses[0]
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        fun getFullAddress(context: Context, latLng: LatLng): String {
            val geocoder = Geocoder(context)
            try {
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses != null && !addresses.isEmpty()) {
                    val address = addresses[0]
                    val addressString: StringBuilder = StringBuilder(address.getAddressLine(0))
                    for (i in 1 until address.maxAddressLineIndex) {
                        addressString.append(", ").append(address.getAddressLine(i))
                    }
                    return addressString.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }
    }

}