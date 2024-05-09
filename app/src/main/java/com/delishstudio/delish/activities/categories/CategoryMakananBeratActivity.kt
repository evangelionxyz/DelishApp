package com.delishstudio.delish.activities.categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.activities.SetupShop
import com.delishstudio.delish.activities.adapters.CategoryListAdapter
import com.delishstudio.delish.databinding.ActivityCategoryFoodBinding
import com.delishstudio.delish.model.FoodModel
import com.delishstudio.delish.model.FoodCategory

class CategoryMakananBeratActivity: AppCompatActivity() {
    private lateinit var mBinding: ActivityCategoryFoodBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCategoryFoodBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupAdapters()
    }

    private fun setupAdapters() {
        val recyclerView: RecyclerView = mBinding.rcCategoryFood
        recyclerView.layoutManager = LinearLayoutManager(this)

        val foodList: ArrayList<FoodModel> = ArrayList()
        for (shop in SetupShop.shops) {
            for(food in shop.foodList) {
                if(food.cat == FoodCategory.MAKANAN_BERAT) {
                    foodList.add(food)
                }
            }
        }

        val adapter = CategoryListAdapter(foodList)
        recyclerView.adapter = adapter
    }
}