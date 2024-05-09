package com.delishstudio.delish.activities.categories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.activities.SetupShop
import com.delishstudio.delish.activities.adapters.CategoryListAdapter
import com.delishstudio.delish.databinding.ActivityCategoryFoodBinding
import com.delishstudio.delish.model.FoodModel
import com.delishstudio.delish.model.FoodCategory

class CategoryVeganActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityCategoryFoodBinding

    private var foodList: ArrayList<FoodModel> = ArrayList()
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
                if(food.cat == FoodCategory.VEGAN) {
                    foodList.add(food)
                }
            }
        }

        val adapter = CategoryListAdapter(foodList)
        recyclerView.adapter = adapter
    }
}