package com.delishstudio.delish.activities.adapters

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.R
import com.delishstudio.delish.databinding.LayoutAddFoodBottomSheetBinding
import com.delishstudio.delish.databinding.RcCategoryFoodBinding
import com.delishstudio.delish.model.FoodModel
import com.delishstudio.delish.model.FoodCategory
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.activities.checkout.CheckoutActivity
import com.delishstudio.delish.system.Utils

class CategoryListAdapter(val foodList: ArrayList<FoodModel>)
    : RecyclerView.Adapter<CategoryListAdapter.FoodHolder>() {

    inner class FoodHolder(binding: RcCategoryFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        var name: TextView = binding.txtName
        var quantity: TextView = binding.txtQuantity
        var category: TextView = binding.txtCategory
        var price: TextView = binding.txtPrice
        var addButton: AppCompatButton = binding.btAdd
        var address: TextView = binding.txtAddress
        var distance: TextView = binding.txtDistance
        var distanceUnit: TextView = binding.txtDistanceUnit
        var bg: LinearLayout = binding.lyImageFrame
        var ratingNumber: TextView = binding.txtRating
        var imgPreview: ImageView = binding.imgFoodImage
        var rewardPoints: TextView = binding.txtRewardPoints
        var isDialogShown = false

        init {
            addButton.setOnClickListener {
                val food = foodList[bindingAdapterPosition]
                val shop = Utils.getShop(food.shopId)
                if(UserManager.Main.addressList.isNotEmpty()) {
                    val trs = UserManager.Main.transaction
                    if (trs.foodList.isNotEmpty()) {
                        val fShop = Utils.getShop(trs.foodList[0].shopId)
                        if (fShop!!.id != shop!!.id) {
                            UserManager.Main.transaction.foodList.clear()
                            // TODO: Create dialog
                            Utils.LongToastText(itemView.context, "Order sebelumnya dihapus")
                        }
                    }
                    showDialog()
                } else {
                    Utils.LongToastText(itemView.context,
                        "Tambahkan alamat untuk memulai belanja")
                }
            }
        }

        private fun showDialog() {

            // Dialog only shown once
            if (isDialogShown || UserManager.Main.mainAddressIndex < 0) {
                return
            }

            val dialog = Dialog(itemView.context)

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val inflater = LayoutInflater.from(itemView.context)
            val dBinding = LayoutAddFoodBottomSheetBinding.inflate(inflater)
            dialog.setContentView(dBinding.root)

            val currentFood = foodList[bindingAdapterPosition]

            if (currentFood.buyQuantity == 0)
                currentFood.buyQuantity++

            dBinding.txtFoodName.text = currentFood.name
            dBinding.txtPrice.text = Utils.idFormatedCurrency(currentFood.price)
            dBinding.txtCounter.text = currentFood.buyQuantity.toString()

            dBinding.imgPreview.setImageResource(currentFood.imgId)

            val shop = Utils.getShop(currentFood.shopId)
            dBinding.txtFoodAddress.text = shop!!.name
            currentFood.orderMsgToRestaurant = dBinding.etRestaurantMsg.text.toString()

            dBinding.btIncrement.setOnClickListener {
                if(currentFood.buyQuantity >= currentFood.quantity) {
                    Utils.LongToastText(itemView.context, "Tersisa " +
                            "${currentFood.quantity}")
                } else {
                    currentFood.buyQuantity++
                }

                dBinding.txtCounter.text = currentFood.buyQuantity.toString()
            }

            dBinding.btDecrement.setOnClickListener{
                if(currentFood.buyQuantity > 0){
                    currentFood.buyQuantity--
                    dBinding.txtCounter.text = currentFood.buyQuantity.toString()
                }

                if(currentFood.buyQuantity == 0){
                    dialog.dismiss()
                }
            }

            dialog.setOnDismissListener {
                isDialogShown = false
            }

            dBinding.btAddFoodOrder.setOnClickListener{
                UserManager.Main.transaction.foodList.add(currentFood)
                UserManager.Main.transaction.calcOrderdFoodCost()

                val intent = Intent(itemView.context, CheckoutActivity::class.java)
                itemView.context.startActivity(intent)
            }

            dialog.show()
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.BottomSheetAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)

            isDialogShown = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RcCategoryFoodBinding.inflate(inflater, parent, false)
        return FoodHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        val food = foodList[position]

        holder.name.text = food.name
        holder.price.text = Utils.idFormatedCurrency(food.price)
        holder.quantity.setText("${food.quantity} ${food.quaUnit}")
        holder.category.text = food.getCategoryString()
        holder.ratingNumber.text = food.rating.toString()

        val shop = Utils.getShop(food.shopId)

        holder.address.text = shop!!.name
        if (shop.name.length > 16) {
            val txt = shop.name.subSequence(0, 16)
            holder.address.text = "${txt}..."
        }

        holder.rewardPoints.text = "+${shop.rewardPoints} Points"

        holder.distanceUnit.text = "m"
        holder.distance.setText(food.distance.toString())

        if(UserManager.Main.mainAddressIndex < 0) {
            holder.distance.text = ""
            holder.distanceUnit.text = ""
        }

        when(food.cat){
            FoodCategory.MAKANAN_BERAT -> {
                food.imgId = R.drawable.img_heavy_food_cat
                holder.bg.setBackgroundResource(R.drawable.background_light_blue)
            }
            FoodCategory.NON_HALAL -> {
                food.imgId = R.drawable.img_non_halal_food
                holder.bg.setBackgroundResource(R.drawable.background_pink)
            }
            FoodCategory.CAMILAN -> {
                food.imgId = R.drawable.img_beverages_cat
                holder.bg.setBackgroundResource(R.drawable.background_orange)
            }
            FoodCategory.MINUMAN -> {
                food.imgId = R.drawable.img_drinks_cat
                holder.bg.setBackgroundResource(R.drawable.background_pink)
            }
            FoodCategory.BAHAN_MAKANAN -> {
                food.imgId = R.drawable.img_groceries_cat
                holder.bg.setBackgroundResource(R.drawable.background_light_blue)
            }
            FoodCategory.VEGAN -> {
                food.imgId = R.drawable.img_vegan_cat
                holder.bg.setBackgroundResource(R.drawable.background_orange)
            }
            FoodCategory.MYSTERY_BOX -> {
                food.imgId = R.drawable.img_mysterybox
                holder.bg.setBackgroundResource(R.drawable.background_light_blue)
            }
        }

        holder.imgPreview.setImageResource(food.imgId)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}
