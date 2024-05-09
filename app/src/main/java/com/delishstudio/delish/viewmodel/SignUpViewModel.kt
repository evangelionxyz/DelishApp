package com.delishstudio.delish.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignUpViewModel: ViewModel() {
    private val mFirebaseAuth = FirebaseAuth.getInstance()

    fun createUserEmailAndPassword(email: String, password: String, verifPassword: String, onSignUpComplete: (Boolean) -> Unit ) {
        if (password == verifPassword) {
            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                onSignUpComplete(it.isSuccessful)
            }
        }else {
            onSignUpComplete(false)
        }
    }

    fun signUpWithGoogle(idToken: String?, onSignInComplete: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            onSignInComplete(task.isSuccessful)
        }
    }
}