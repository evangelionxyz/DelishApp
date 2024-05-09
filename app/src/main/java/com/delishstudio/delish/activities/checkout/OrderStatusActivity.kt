package com.delishstudio.delish.activities.checkout

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.MainActivity
import com.delishstudio.delish.activities.SetupShop
import com.delishstudio.delish.activities.adapters.OrderStatusListAdapter
import com.delishstudio.delish.databinding.ActivityOrderStatusBinding
import com.delishstudio.delish.model.TransactionModel
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.system.DatabaseStrRef
import com.delishstudio.delish.system.LocationUtils
import com.delishstudio.delish.system.Utils
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase

class OrderStatusActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityOrderStatusBinding
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: OrderStatusListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityOrderStatusBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val trs = UserManager.Main.transaction

        mBinding.btBack.setOnClickListener{
            super.onBackPressedDispatcher.onBackPressed()
        }

        mBinding.btSendToRestaurant.setOnClickListener{
            updateStatus()

            var databaseRef = FirebaseDatabase.getInstance()
                .getReference(DatabaseStrRef.SHOPS)

            for (shop in SetupShop.shops) {
                var isChanged = false

                for (dFood in shop.foodList) {
                    for (food in trs.foodList) {
                        if(food.id.isNotEmpty()) {
                            if(dFood.id == food.id) {
                                dFood.quantity -= food.buyQuantity
                                food.quantity = dFood.quantity
                                isChanged = true
                            }
                        }
                    }
                }

                for (food in trs.foodList) {
                    if(food.quantity <= 0) {
                        shop.foodList.remove(food)
                        isChanged = true
                    }
                }

                if(isChanged) {
                    databaseRef.child(shop.id).setValue(shop)
                }
            }

            databaseRef = FirebaseDatabase.getInstance()
                .getReference(DatabaseStrRef.VOUCHERS)

            var index = -1
            for (i in 0..<SetupShop.vouchers.size) {
                if(trs.voucher != null) {
                    val v = SetupShop.vouchers[i]
                    if (v.id == trs.voucher!!.id) {
                        databaseRef.child(v.id).removeValue()
                        index = i
                        break
                    }
                }
            }

            if (index != -1) {
                SetupShop.vouchers.removeAt(index)
            }

            trs.updateTransactionTime()

            val t = TransactionModel()
            for (food in trs.foodList) {
                t.foodList.add(food)
            }

            t.totalRewardPoints = trs.totalRewardPoints
            t.courier = trs.courier
            t.voucher = trs.voucher
            t.pickup = trs.pickup
            t.transactionTime = trs.transactionTime
            t.totalCost = trs.totalCost
            t.grandTotal = trs.grandTotal
            t.deliveryCost = trs.deliveryCost

            UserManager.Main.transactionList.add(t)
            trs.reset()
            UserManager.Update()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val shop = Utils.getShop(trs.foodList[0].shopId)
        mBinding.txtRestaurantName.text = shop!!.name

        val address = LocationUtils.getFullAddress(this, LatLng(shop.latitude, shop.longitude))

        mBinding.txtRestaurantAddress.text = address
        mBinding.txtSubTotal.text = Utils.idFormatedCurrency(trs.totalCost)

        mBinding.txtDeliveryCostPrice.text = Utils.idFormatedCurrency(trs.deliveryCost)
        mBinding.txtDiscount.text = Utils.idFormatedCurrency(-trs.discountInPrice)

        if (trs.voucher != null) {
            mBinding.txtVoucherCode.text = trs.voucher!!.code
        }

        trs.grandTotal = trs.totalCost + trs.deliveryCost - trs.discountInPrice
        trs.totalRewardPoints = shop.rewardPoints

        mBinding.txtGrandTotal.text = Utils.idFormatedCurrency(trs.grandTotal)

        UserManager.Main.rewardPoints += trs.totalRewardPoints

        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val density = resources.displayMetrics.density
        layoutParams.topMargin = (8 * density).toInt()

        if (!trs.pickup) {
            layoutParams.width = 0
            layoutParams.height = 0
            layoutParams.topMargin = 0
        } else {
            val p = LinearLayout.LayoutParams(0, 0)
            mBinding.txtDeliveryCost.layoutParams = p
            mBinding.txtDeliveryCostPrice.layoutParams = p
        }

        mBinding.layoutPickupCode.layoutParams = layoutParams

        setupAdapter()
    }

    private fun updateStatus() {
        //mBinding.txtStatus.
    }

    private fun setupAdapter() {
        mRecyclerView = mBinding.rcOrderList
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mAdapter = OrderStatusListAdapter()
        mRecyclerView.adapter = mAdapter
    }
}