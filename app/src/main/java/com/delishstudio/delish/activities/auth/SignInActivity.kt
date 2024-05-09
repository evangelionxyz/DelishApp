package com.delishstudio.delish.activities.auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.delishstudio.delish.MainActivity
import com.delishstudio.delish.R
import com.delishstudio.delish.databinding.ActivitySignInBinding
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.system.Utils
import com.delishstudio.delish.viewmodel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySignInBinding
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mGooGleSignInClient: GoogleSignInClient
    private var userPassword: String = ""

    private val mViewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mBinding.btCreateAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        mBinding.btEmailSignIn.setOnClickListener {
            emailPasswordSignIn()
        }

        configureGoogleSignIn()
    }

    private fun emailPasswordSignIn() {
        val email = mBinding.etEmail.text.toString()
        userPassword = mBinding.etPassword.text.toString()

        if (email.isNotEmpty() && userPassword.isNotEmpty()) {
            mViewModel.signInWithEmail(email, userPassword) {success ->
                if(success) {
                    startMainNextScreen()
                }
                else {
                    Toast.makeText(this, "Your email or password is wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            Toast.makeText(this, "Empty fields is not allowed!!", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }

    private fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_sign_in_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        mGooGleSignInClient = GoogleSignIn.getClient(this, gso)

        mBinding.btGoogleSignIn.setOnClickListener {
            val signInIntent = mGooGleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            mViewModel.signInWithGoogle(account?.idToken) {success ->
                if(success) {
                    startMainNextScreen()
                }
                else {
                    Utils.LongToastText(this, "Sign in with Google failed")
                }
            }
        }
        catch (e: ApiException) {
            Utils.LongToastText(this, e.toString())
        }
    }

    private fun startMainNextScreen() {
        UserManager.Available { userAvailable ->
            val intent = if (!userAvailable) {
                Intent(this, UserSetupActivity::class.java)
            }
            else {
                Intent(this, MainActivity::class.java)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("userPassword", userPassword)
            startActivity(intent)
            finish()
        }
    }
}