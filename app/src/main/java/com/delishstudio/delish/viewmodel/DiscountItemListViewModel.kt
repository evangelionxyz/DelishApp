package com.delishstudio.delish.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delishstudio.delish.model.FoodModel

class DiscountItemListViewModel: ViewModel() {

    val liveDataList = MutableLiveData<ArrayList<FoodModel>>()

    companion object {
        val foodList = ArrayList<FoodModel>()
    }

    init {
        liveDataList.value = foodList
    }
}