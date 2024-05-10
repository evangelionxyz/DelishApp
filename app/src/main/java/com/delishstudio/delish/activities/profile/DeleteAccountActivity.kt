package com.delishstudio.delish.activities.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.delishstudio.delish.R
import com.delishstudio.delish.databinding.ActivityDeleteAccountBinding
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.activities.auth.AuthBoardingActivity
import com.delishstudio.delish.system.DatabaseStrRef
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class DeleteAccountActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDeleteAccountBinding
    private var mIsDialogShown: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        supportActionBar?.hide()
        mBinding = ActivityDeleteAccountBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.txtUsername.text = UserManager.Main.userName
        mBinding.txtUserEmail.text = UserManager.Main.email
        mBinding.txtUserPhone.text = UserManager.Main.phone

        setupButtons()
    }

    private fun setupButtons() {
        mBinding.btBack.setOnClickListener {
            super.onBackPressed()
        }

        mBinding.btDeleteAccount.setOnClickListener {
            showDeleteAccountConfirmation()
        }
    }

    private fun showDeleteAccountConfirmation() {
        if(mIsDialogShown)
            return

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_confirmation)
        val confirmationText = dialog.findViewById<TextView>(R.id.txtConfirmationStatus)
        confirmationText.text = "Hapus Akun"
        val questionText = dialog.findViewById<TextView>(R.id.txtConfirmationQuestion)
        questionText.text = "Apa kamu yakin mau lanjut hapus akun kamu di aplikasi Delish?"
        val confirmBtn = dialog.findViewById<AppCompatButton>(R.id.btConfirm)
        confirmBtn.text = "Hapus"

        val cancelBtn = dialog.findViewById<AppCompatButton>(R.id.btCancel)

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user = UserManager.Main

        if (user.password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
        } else {
            firebaseAuth.currentUser!!.getIdToken(true).addOnCompleteListener { task ->
                val idToken = task.result.token
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(credential)
            }
        }

        confirmBtn.setOnClickListener {
            firebaseAuth.currentUser!!.delete().addOnCompleteListener { task->
                if (task.isSuccessful) {
                    val userRef = FirebaseDatabase.getInstance()
                        .getReference("${DatabaseStrRef.USERS}/${UserManager.Main.userId}")
                    userRef.removeValue()

                    val intent = Intent(this, AuthBoardingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(this, "Login again to delete your account", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }

        cancelBtn.setOnClickListener{
            dialog.dismiss()
        }

        dialog.setOnDismissListener{
            mIsDialogShown = false
        }

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)

        dialog.show()
        mIsDialogShown = true
    }
}