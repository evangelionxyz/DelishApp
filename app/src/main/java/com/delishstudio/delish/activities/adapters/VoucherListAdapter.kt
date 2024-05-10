package com.delishstudio.delish.activities.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.activities.checkout.CheckoutActivity
import com.delishstudio.delish.databinding.RcVoucherListBinding
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.model.VoucherModel
import com.delishstudio.delish.system.Utils

class VoucherListAdapter(var vouchers: ArrayList<VoucherModel>, val clickable: Boolean) : RecyclerView.Adapter<VoucherListAdapter.VoucherHolder>() {

    inner class VoucherHolder(binding: RcVoucherListBinding) : RecyclerView.ViewHolder(binding.root) {
        var name: TextView = binding.txtVoucherName
        var discount: TextView = binding.txtDiscount
        var minSpend: TextView = binding.txtMinSpend
        var layout: LinearLayout = binding.lyVoucherList

        init {

            if (clickable) {
                layout.setOnClickListener {
                    val voucher = vouchers[bindingAdapterPosition]
                    val tr = UserManager.Main.transaction

                    if (tr.totalCost > voucher.minSpend) {
                        UserManager.Main.transaction.voucher = vouchers[bindingAdapterPosition]
                        notifyItemChanged(bindingAdapterPosition)
                    } else {
                        Utils.ShortToastText(itemView.context, "Minimum belanja masih kurang")
                    }

                    val intent = Intent(itemView.context, CheckoutActivity::class.java)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherHolder {
        val binding = RcVoucherListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoucherHolder(binding)
    }

    override fun getItemCount(): Int {
        return vouchers.size
    }

    override fun onBindViewHolder(holder: VoucherHolder, position: Int) {
        val voucher = vouchers[position]
        holder.name.text = voucher.code
        holder.discount.text = "Diskon ${voucher.discountPercent}% hingga Rp${Utils.idFormatedCurrency(voucher.maxSpend)}"
        holder.minSpend.text = "Min belanja Rp${Utils.idFormatedCurrency(voucher.minSpend)}"
    }
}