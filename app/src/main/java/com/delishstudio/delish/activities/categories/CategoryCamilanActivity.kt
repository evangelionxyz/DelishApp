package com.delishstudio.delish.activities.categories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.R
import com.delishstudio.delish.activities.SetupShop
import com.delishstudio.delish.activities.adapters.CategoryListAdapter
import com.delishstudio.delish.databinding.ActivityCategoryFoodBinding
import com.delishstudio.delish.model.FoodModel
import com.delishstudio.delish.model.FoodCategory

class CategoryCamilanActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityCategoryFoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCategoryFoodBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupButtons()
        setupAdapters()
    }

    private fun setupButtons() {
        mBinding.btNearby.setOnClickListener{
            mBinding.btNearby.setTextColor(getColor(R.color.white))
            mBinding.btNearby.backgroundTintList = ContextCompat.getColorStateList(this, R.color.dark_green)
            mBinding.btRecomendation.setTextColor(getColor(R.color.black_erie))
            mBinding.btRecomendation.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
        }

        mBinding.btRecomendation.setOnClickListener{
            mBinding.btRecomendation.setTextColor(getColor(R.color.white))
            mBinding.btRecomendation.backgroundTintList = ContextCompat.getColorStateList(this, R.color.dark_green)
            mBinding.btNearby.setTextColor(getColor(R.color.black_erie))
            mBinding.btNearby.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
        }
    }

    private fun setupAdapters() {
        val recyclerView: RecyclerView = mBinding.rcCategoryFood
        recyclerView.layoutManager = LinearLayoutManager(this)

        val foodList: ArrayList<FoodModel> = ArrayList()
        for (shop in SetupShop.shops) {
            for(food in shop.foodList) {
                if(food.cat == FoodCategory.CAMILAN) {
                    foodList.add(food)
                }
            }
        }
        val adapter = CategoryListAdapter(foodList)
        recyclerView.adapter = adapter
    }
}