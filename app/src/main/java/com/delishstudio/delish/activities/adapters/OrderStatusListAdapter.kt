package com.delishstudio.delish.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.R
import com.delishstudio.delish.databinding.RcOrderStatusListBinding
import com.delishstudio.delish.model.FoodCategory
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.system.Utils

class OrderStatusListAdapter()
: RecyclerView.Adapter<OrderStatusListAdapter.FoodHolder>() {

    inner class FoodHolder(binding: RcOrderStatusListBinding): RecyclerView.ViewHolder(binding.root) {
        var name: TextView = binding.orderedFoodName
        var price: TextView = binding.orderedFoodPrice
        var counter: TextView = binding.orderedCounter
        var address: TextView = binding.orderedFoodAddress
        var imgPreview: ImageView = binding.imgPreview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val binding = RcOrderStatusListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodHolder(binding)
    }

    override fun getItemCount(): Int {
        return  UserManager.Main.transaction.foodList.size
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        val food =  UserManager.Main.transaction.foodList[position]
        holder.name.text = food.name

        val shop = Utils.getShop(food.shopId)
        if (shop != null)  {
            holder.address.text = shop.name
        }

        holder.price.text = Utils.idFormatedCurrency(food.price)
        holder.counter.text = "${food.buyQuantity} items"

        when(food.cat){
            FoodCategory.MAKANAN_BERAT -> {
                food.imgId = R.drawable.img_heavy_food_cat
            }
            FoodCategory.NON_HALAL -> {
                food.imgId = R.drawable.img_non_halal_food
            }
            FoodCategory.CAMILAN -> {
                food.imgId = R.drawable.img_beverages_cat
            }
            FoodCategory.MINUMAN -> {
                food.imgId = R.drawable.img_drinks_cat
            }
            FoodCategory.BAHAN_MAKANAN -> {
                food.imgId = R.drawable.img_groceries_cat
            }
            FoodCategory.VEGAN -> {
                food.imgId = R.drawable.img_vegan_cat
            }
            FoodCategory.MYSTERY_BOX -> {
                food.imgId = R.drawable.img_mysterybox
            }
        }

        holder.imgPreview.setImageResource(food.imgId)
    }
}