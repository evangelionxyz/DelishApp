package com.delishstudio.delish.activities.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.delishstudio.delish.MainActivity
import com.delishstudio.delish.R
import com.delishstudio.delish.databinding.ActivityEditProfileBinding
import com.delishstudio.delish.model.UserManager

class EditProfileActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        supportActionBar?.hide()
        setContentView(mBinding.root)

        mBinding.etUserName.setText(UserManager.Main.userName)
        mBinding.txtUserEmail.text = UserManager.Main.email
        mBinding.etUserPhone.setText(UserManager.Main.phone)

        if(UserManager.Main.profileImageUrl.isNotEmpty()) {
            Glide.with(this).load(UserManager.Main.profileImageUrl).into(mBinding.imgUserProfileImage);
        }

        mBinding.btBack.setOnClickListener {
            super.onBackPressedDispatcher.onBackPressed()
        }

        // TODO: Implement Change Profile Picture
        //mBinding.btChangeProfilePicture.setOnClickListener {
        //}

        mBinding.btSave.setOnClickListener {
            UserManager.Main.userName = mBinding.etUserName.text.toString()
            UserManager.Main.phone = mBinding.etUserPhone.text.toString()
            UserManager.Update()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}