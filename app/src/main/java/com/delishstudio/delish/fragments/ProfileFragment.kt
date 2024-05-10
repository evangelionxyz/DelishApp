package com.delishstudio.delish.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.delishstudio.delish.R
import com.delishstudio.delish.activities.checkout.VoucherActivity
import com.delishstudio.delish.databinding.FragmentProfileBinding
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.activities.profile.AccountSettingsActivity
import com.delishstudio.delish.activities.profile.ChangeLanguageActivity
import com.delishstudio.delish.activities.profile.EditProfileActivity
import com.delishstudio.delish.activities.profile.PaymentMethodActivity
import com.delishstudio.delish.activities.profile.SavedAddressActivity

class ProfileFragment : Fragment() {
    private lateinit var mBinding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.txtUsername.text = UserManager.Main.userName
        mBinding.txtUserEmail.text = UserManager.Main.email
        mBinding.txtUserPhone.text = UserManager.Main.phone
        mBinding.txtUserRewardPoints.text = UserManager.Main.rewardPoints.toString()

        if(UserManager.Main.profileImageUrl.isNotEmpty()) {
            Glide.with(this).load(UserManager.Main.profileImageUrl).into(mBinding.imgUserProfileImage);
        } else {
            mBinding.imgUserProfileImage.setImageResource(R.drawable.ic_user)
        }

        onSetupButtons()
    }

    private fun onSetupButtons() {

        mBinding.btPaymentMethod.setOnClickListener{
            val intent = Intent(activity, PaymentMethodActivity::class.java)
            startActivity(intent)
        }

        mBinding.btSavedAddress.setOnClickListener {
            val intent = Intent(activity, SavedAddressActivity::class.java)
            startActivity(intent)
        }

        mBinding.btAccountSettings.setOnClickListener {
            val intent = Intent(activity, AccountSettingsActivity::class.java)
            startActivity(intent)
        }

        mBinding.btPromos.setOnClickListener {
            val intent = Intent(activity, VoucherActivity::class.java)
            intent.putExtra("isClickable", false)
            startActivity(intent)
        }

        mBinding.btChangeLanguage.setOnClickListener{
            val intent = Intent(activity, ChangeLanguageActivity::class.java)
            startActivity(intent)
        }

        mBinding.btEditProfile.setOnClickListener{
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}