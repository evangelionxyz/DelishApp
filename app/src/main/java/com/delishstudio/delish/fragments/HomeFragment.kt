package com.delishstudio.delish.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.delishstudio.delish.databinding.FragmentHomeBinding
import com.delishstudio.delish.activities.categories.CategoryBahanMakananActivity
import com.delishstudio.delish.activities.categories.CategoryCamilanActivity
import com.delishstudio.delish.activities.categories.CategoryMakananBeratActivity
import com.delishstudio.delish.activities.categories.CategoryMinumanActivity
import com.delishstudio.delish.activities.categories.CategoryNonHalalActivity
import com.delishstudio.delish.activities.categories.CategoryVeganActivity
import com.delishstudio.delish.activities.adapters.DiscountItemListAdapter
import com.delishstudio.delish.activities.checkout.CheckoutActivity
import com.delishstudio.delish.activities.profile.MapPinpointActivity
import com.delishstudio.delish.viewmodel.DiscountItemListViewModel

class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mAdapter: DiscountItemListAdapter
    private val mViewModel: DiscountItemListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()

        mBinding.rcDiscountList.apply {
            layoutManager = LinearLayoutManager(activity)
            mAdapter = DiscountItemListAdapter()
            adapter = mAdapter
        }

        mViewModel.liveDataList.observe(viewLifecycleOwner) { response ->
            mAdapter.itemDiffer.submitList(response)
        }
    }

    private fun setupButtons() {

        mBinding.btHeavyFood.setOnClickListener {
            val intent = Intent(activity, CategoryMakananBeratActivity::class.java)
            startActivity(intent)
        }

        mBinding.btDrinks.setOnClickListener {
            val intent = Intent(activity, CategoryMinumanActivity::class.java)
            startActivity(intent)
        }

        mBinding.btBeverages.setOnClickListener {
            val intent = Intent(activity, CategoryCamilanActivity::class.java)
            startActivity(intent)
        }

        mBinding.btVegan.setOnClickListener {
            val intent = Intent(activity, CategoryVeganActivity::class.java)
            startActivity(intent)
        }

        mBinding.btNonHalal.setOnClickListener {
            val intent = Intent(activity, CategoryNonHalalActivity::class.java)
            startActivity(intent)
        }

        mBinding.btGroceries.setOnClickListener {
            val intent = Intent(activity, CategoryBahanMakananActivity::class.java)
            startActivity(intent)
        }

        mBinding.btCart.setOnClickListener {
            val intent = Intent(activity, CheckoutActivity::class.java)
            startActivity(intent)
        }

        mBinding.btAddress.setOnClickListener{
            val intent = Intent(activity, MapPinpointActivity::class.java)
            startActivity(intent)
        }
    }
}