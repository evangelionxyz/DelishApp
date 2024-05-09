package com.delishstudio.delish.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TransactionModel {
    var foodList: ArrayList<FoodModel> = ArrayList()
    var courier: DeliveryCourier = DeliveryCourier.GOSEND
    var voucher: VoucherModel? = null
    var pickup: Boolean = false
    var deliveryCost: Int = 0
    var transactionTime: String = ""
    var totalRewardPoints: Int = 0
    var totalCost: Int = 0
    var discountInPrice: Int = 0
    var grandTotal: Int = 0

    fun updateTransactionTime() {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
        transactionTime = currentDateTime.format(formatter)
    }

    fun calcOrderdFoodCost() {
        totalCost = 0
        for (f in foodList) {
            totalCost += f.price * f.buyQuantity
        }
    }

    fun reset() {
        foodList.clear()
        totalCost = 0
        pickup = false
        totalRewardPoints = 0
        transactionTime = ""

        if(voucher != null)
            voucher = null
    }
}