package com.delishstudio.delish.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delishstudio.delish.R
import com.delishstudio.delish.activities.SetupShop
import com.delishstudio.delish.databinding.FragmentMysteryBoxBinding
import com.delishstudio.delish.model.FoodCategory
import com.delishstudio.delish.model.FoodModel
import com.delishstudio.delish.activities.adapters.CategoryListAdapter
import com.delishstudio.delish.model.UserManager

class MysteryBoxFragment : Fragment() {
    private lateinit var mBinding: FragmentMysteryBoxBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentMysteryBoxBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()

        showDialog()
    }

    private fun showDialog() {
        if(UserManager.Main.dismissedMysteryWarn) {
            return
        }

        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_mysterybox_warning)

        val gotItBtn: CardView = dialog.findViewById(R.id.btMBWarningGotIt)

        gotItBtn.setOnClickListener{
            dialog.dismiss()
        }

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)

        dialog.show()

        UserManager.Main.dismissedMysteryWarn = true
        UserManager.Update()
    }

    private fun setupAdapters() {
        val recyclerView: RecyclerView = mBinding.rcMisteryBox
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val foodList: ArrayList<FoodModel> = ArrayList()
        for (shop in SetupShop.shops) {
            for(food in shop.foodList) {
                if(food.cat == FoodCategory.MYSTERY_BOX) {
                    foodList.add(food)
                }
            }
        }

        val adapter = CategoryListAdapter(foodList)
        recyclerView.adapter = adapter
    }
}