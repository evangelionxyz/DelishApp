package com.delishstudio.delish.model

import com.delishstudio.delish.system.LocationUtils

class ShopModel(var name: String = "") {
    var id: String = ""
    var foodList: ArrayList<FoodModel> = ArrayList()
    var deliveryCost: Int = 0
    var rewardPoints: Int = 0
    var longitude: Double = 0.0
    var latitude: Double = 0.0

    fun updateDistance(toLat: Double, toLng: Double) {
        for(food in foodList) {
            food.distance = LocationUtils.distanceTo(
                toLat,
                toLng,
                latitude,
                longitude
            )
        }
    }

    fun addFood(food: FoodModel) {
        food.distance = LocationUtils.distanceTo(
            UserManager.Main.latitude,
            UserManager.Main.longitude,
            latitude,
            longitude
        )
        foodList.add(food)
    }
}