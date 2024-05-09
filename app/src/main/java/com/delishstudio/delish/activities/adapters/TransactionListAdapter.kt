package com.delishstudio.delish.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.databinding.RcRecentTransactionListBinding
import com.delishstudio.delish.model.TransactionModel
import com.delishstudio.delish.system.Utils

class TransactionListAdapter (var transactions: ArrayList<TransactionModel>)
    : RecyclerView.Adapter<TransactionListAdapter.TransactionHolder>() {

    inner class TransactionHolder(val binding: RcRecentTransactionListBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val shopName = binding.recentTrsShopName
        val itemCount = binding.recentTrsItemCount
        val rewardPoints = binding.recentTrsRewardPoints
        val transactionTime = binding.recentTrsTransactionTime
        val totalCost = binding.recentTrsTotalCost
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RcRecentTransactionListBinding.inflate(inflater, parent, false)
        return TransactionHolder(binding)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val trs = transactions[position]
        val shop = Utils.getShop(trs.foodList[0].shopId)
        holder.shopName.text = shop!!.name

        if (shop.name.length > 32) {
            val txt = shop.name.subSequence(0, 32)
            holder.shopName.text = "${txt}..."
        }

        holder.itemCount.setText("${trs.foodList.size} items")
        holder.rewardPoints.setText("+${trs.totalRewardPoints} points")
        holder.transactionTime.text = trs.transactionTime
        holder.totalCost.text = Utils.idFormatedCurrency(trs.grandTotal)
    }
}