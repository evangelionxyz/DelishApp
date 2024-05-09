package com.delishstudio.delish.activities.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.activities.SetupShop
import com.delishstudio.delish.databinding.ActivityVoucherBinding
import com.delishstudio.delish.activities.adapters.VoucherListAdapter

class VoucherActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityVoucherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityVoucherBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupAdapters()
    }

    private fun setupAdapters() {
        val recyclerView: RecyclerView = mBinding.rcVoucherList
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = VoucherListAdapter(SetupShop.vouchers)

        recyclerView.adapter = adapter
    }
}