package com.delishstudio.delish.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delishstudio.delish.model.FoodCategory
import com.delishstudio.delish.model.FoodModel

class DiscountItemListViewModel: ViewModel() {

    val liveDataList = MutableLiveData<ArrayList<FoodModel>>()

    private var recyclerItems = ArrayList<FoodModel>()

    init {
        fetchSampleData()
    }

    private fun fetchSampleData() {
        recyclerItems.clear()
        recyclerItems.add(FoodModel("Donat", 12, 5000, FoodCategory.CAMILAN))
        recyclerItems.add(FoodModel("Bir", 12, 40000, FoodCategory.MINUMAN))
        liveDataList.value = recyclerItems
    }
}