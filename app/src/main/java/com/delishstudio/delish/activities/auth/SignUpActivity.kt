package com.delishstudio.delish.activities.auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.delishstudio.delish.MainActivity
import com.delishstudio.delish.R
import com.delishstudio.delish.databinding.ActivitySignUpBinding
import com.delishstudio.delish.model.UserManager
import com.delishstudio.delish.system.Utils
import com.delishstudio.delish.viewmodel.SignUpViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySignUpBinding
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mGooGleSignInClient: GoogleSignInClient
    private val mViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mFirebaseAuth = FirebaseAuth.getInstance()

        // NOT A USER BUTTON
        mBinding.btLogin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        // SIGN UP BUTTON
        mBinding.btEmailSignup.setOnClickListener {
            val email = mBinding.etEmail.text.toString()
            val password = mBinding.etPassword.text.toString()
            val verifPassword = mBinding.etPasswordVerification.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && verifPassword.isNotEmpty()) {
                mViewModel.createUserEmailAndPassword(email, password, verifPassword) {success ->
                    if (success) {
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                    }
                    else Utils.LongToastText(this, "Password is not matching")
                }
            }
            else Utils.LongToastText(this, "Empty fields is not allowed!!")
        }

        configureGoogleSignUp()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }

    private fun configureGoogleSignUp() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_sign_in_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        mGooGleSignInClient = GoogleSignIn.getClient(this, gso)

        mBinding.btGoogleSignUp.setOnClickListener {
            val signInIntent = mGooGleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            mViewModel.signUpWithGoogle(account?.idToken) {success ->
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
            startActivity(intent)
            finish()
        }
    }
}