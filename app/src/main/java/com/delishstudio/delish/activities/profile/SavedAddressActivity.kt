package com.delishstudio.delish.activities.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.activities.adapters.SavedAddressListAdapter
import com.delishstudio.delish.databinding.ActivitySavedAddressBinding
import com.delishstudio.delish.model.UserManager

class SavedAddressActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySavedAddressBinding
    private lateinit var mAdapter: SavedAddressListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySavedAddressBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupAdapter()
        setupButtons()
    }

    private fun setupAdapter() {
        val recyclerView: RecyclerView = mBinding.rcSavedAddressList
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = SavedAddressListAdapter(UserManager.Main.addressList)
        recyclerView.adapter = mAdapter
    }

    private fun setupButtons() {
        mBinding.btBack.setOnClickListener {
            super.onBackPressedDispatcher.onBackPressed()
        }

        mBinding.btAdd.setOnClickListener {
            val intent = Intent(this, SetupAddressActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}