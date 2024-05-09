package com.delishstudio.delish.activities.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.delishstudio.delish.MainActivity
import com.delishstudio.delish.R
import com.delishstudio.delish.databinding.ActivityUserSetupBinding
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.model.UserModel
import com.delishstudio.delish.system.DatabaseStrRef
import com.delishstudio.delish.system.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserSetupActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityUserSetupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        supportActionBar?.hide()

        mBinding = ActivityUserSetupBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val firebase = FirebaseAuth.getInstance()
        val userRef = FirebaseDatabase.getInstance().getReference(DatabaseStrRef.USERS)

        val photoUrl = firebase.currentUser?.photoUrl
        if (photoUrl != null) {
            Glide.with(this).load(firebase.currentUser?.photoUrl.toString())
                .into(mBinding.imgUserProfileImage);
        } else {
            mBinding.imgUserProfileImage.setImageResource(R.drawable.ic_user)
        }

        mBinding.btNext.setOnClickListener {
            if (mBinding.etUserName.text.isNotEmpty() && mBinding.etPhone.text.isNotEmpty()) {
                val userId = userRef.push().key ?: " "
                val username = mBinding.etUserName.text.toString()
                val phone = mBinding.etPhone.text.toString()
                val email = firebase.currentUser!!.email.toString()

                val password = intent.getStringExtra("userPassword")

                UserManager.Main = UserModel(username, phone, email, userId)
                UserManager.Main.password = password!!
                UserManager.Main.profileImageUrl = if (photoUrl != null) photoUrl.toString() else ""

                userRef.child(userId).setValue(UserManager.Main)
                    .addOnCompleteListener {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()

                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to insert data", Toast.LENGTH_LONG)
                            .show()
                    }
            }
            else {
                Utils.ShortToastText(this, "Please Provide Username and Phone number")
            }
        }
    }
}