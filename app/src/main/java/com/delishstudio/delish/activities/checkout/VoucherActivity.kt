package com.delishstudio.delish.activities.checkout

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
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

        mBinding.btBack.setOnClickListener{
            super.onBackPressedDispatcher.onBackPressed()
        }

        setupAdapters()
    }

    private fun setupAdapters() {
        val recyclerView: RecyclerView = mBinding.rcVoucherList
        recyclerView.layoutManager = LinearLayoutManager(this)

        val clickable = intent.getBooleanExtra("isClickable", false)
        if (!clickable) {
            val params = LinearLayout.LayoutParams(0, 0)
            params.topMargin = 0
            mBinding.layoutUseVoucher.layoutParams = params
        }

        val adapter = VoucherListAdapter(SetupShop.vouchers, clickable)

        recyclerView.adapter = adapter
    }
}