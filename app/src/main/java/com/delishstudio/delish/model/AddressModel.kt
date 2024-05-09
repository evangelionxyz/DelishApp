package com.delishstudio.delish.model

import com.google.android.gms.maps.model.LatLng

class AddressModel(
    var label: String? = null,
    var receipentName: String? = null,
    var receipentPhone: String? = null,
    var addressDetails: String? = null,
) {
    var completeAddress: String? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var id: String? = null
}