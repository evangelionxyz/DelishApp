package com.delishstudio.delish.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.R
import com.delishstudio.delish.databinding.RcOrderedFoodListBinding
import com.delishstudio.delish.model.FoodCategory
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.system.Utils

class CheckoutListAdapter()
    : RecyclerView.Adapter<CheckoutListAdapter.FoodHolder>() {

    interface OnUpdateListener {
        fun onListUpdate()
    }

    private var onUpdateListener: OnUpdateListener? = null
    fun setOnUpdateListener(listener: OnUpdateListener) {
        onUpdateListener = listener
    }

    inner class FoodHolder(binding: RcOrderedFoodListBinding): RecyclerView.ViewHolder(binding.root) {
        var name: TextView = binding.orderedFoodName
        var price: TextView = binding.orderedFoodPrice
        var counter: TextView = binding.orderedCounter
        var address: TextView = binding.orderedFoodAddress
        var incrementBtn: Button = binding.orderedIncrementBtn
        var decrementBtn: Button = binding.orderedDecrementBtn
        var imgPreview: ImageView = binding.imgPreview

        init {
            setupButtons()
        }

        private fun setupButtons() {

            incrementBtn.setOnClickListener{
                val currentFood = UserManager.Main.transaction.foodList[bindingAdapterPosition]
                if(currentFood.buyQuantity >= currentFood.quantity) {
                    Utils.LongToastText(itemView.context, "Tersisa " +
                            "${currentFood.quantity}")
                } else {
                    currentFood.buyQuantity++
                }

                UserManager.Main.transaction.calcOrderdFoodCost()
                counter.text = currentFood.buyQuantity.toString()
                onUpdateListener?.onListUpdate() // Notify listener
            }

            decrementBtn.setOnClickListener{
                val currentFood = UserManager.Main.transaction.foodList[bindingAdapterPosition]
                if (currentFood.buyQuantity > 0) {
                    currentFood.buyQuantity--
                    UserManager.Main.transaction.calcOrderdFoodCost()
                    counter.text = currentFood.buyQuantity.toString()
                }

                // delete food if buy quantity is less than 1
                if (currentFood.buyQuantity < 1) {
                    UserManager.Main.transaction.foodList.remove(currentFood)
                    counter.text = "0"
                    Toast.makeText(itemView.context, "${currentFood.name} Food removed", Toast.LENGTH_SHORT).show()
                }
                onUpdateListener?.onListUpdate() // Notify listener
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val binding = RcOrderedFoodListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodHolder(binding)
    }

    override fun getItemCount(): Int {
        return  UserManager.Main.transaction.foodList.size
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        val food =  UserManager.Main.transaction.foodList[position]
        holder.name.text = food.name

        val shop = Utils.getShop(food.shopId)
        if (shop != null) {
            holder.address.text = shop.name
        }

        holder.price.text = Utils.idFormatedCurrency(food.price)
        holder.counter.text = food.buyQuantity.toString()

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