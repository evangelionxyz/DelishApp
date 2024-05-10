package com.delishstudio.delish.activities.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.model.FoodModel
import com.delishstudio.delish.activities.checkout.CheckoutActivity
import com.delishstudio.delish.databinding.RcDiscountItemListBinding
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.system.Utils

// FoodList untuk list semua makanan yang ada di aplikasi
class DiscountItemListAdapter: RecyclerView.Adapter<DiscountItemListAdapter.ViewHolder>(){

    inner class ViewHolder(val mBinding: RcDiscountItemListBinding) : RecyclerView.ViewHolder(mBinding.root) {
        var name: TextView = mBinding.txtFoodListFoodName
        var quantity: TextView = mBinding.txtFoodListAvailQuantity
        var category: TextView = mBinding.txtFoodListCategory
        var price: TextView = mBinding.txtFoodListPrice
        var buyButton: AppCompatButton = mBinding.btFoodListBuy

        init {
            buyButton.setOnClickListener{
                val currentFood = itemDiffer.currentList[bindingAdapterPosition]

                val user = UserManager.Main
                val tr = user.transaction
                if (tr.foodList.isNotEmpty()) {
                    if (!tr.foodList[0].discount) {
                        user.transaction.foodList.clear()
                    }
                }

                currentFood.buyQuantity++
                tr.foodList.add(currentFood)
                tr.calcOrderdFoodCost()

                val intent = Intent(itemView.context, CheckoutActivity::class.java)
                itemView.context.startActivity(intent)
            }
        }
    }

    private val itemDifferCallback = object: DiffUtil.ItemCallback<FoodModel>(){
        override fun areItemsTheSame(oldItem: FoodModel, newItem: FoodModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FoodModel, newItem: FoodModel): Boolean {
            return oldItem == newItem
        }

    }

    val itemDiffer = AsyncListDiffer(this, itemDifferCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RcDiscountItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemDiffer.currentList[position]
        with(holder) {
            mBinding.apply {
                name.text = item.name
                price.text = Utils.idFormatedCurrency(item.price)
                quantity.text = "${item.quantity} ${item.quaUnit}"
                category.text = item.getCategoryString()
            }
        }
    }

    override fun getItemCount(): Int = itemDiffer.currentList.size
}