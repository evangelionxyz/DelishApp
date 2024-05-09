package com.delishstudio.delish.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInViewModel: ViewModel() {

    private val mFirebaseAuth = FirebaseAuth.getInstance()

    fun signInWithEmail(email: String, password: String, onSignInComplete: (Boolean) -> Unit) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                onSignInComplete(task.isSuccessful)
            }
    }

    fun signInWithGoogle(idToken: String?, onSignInComplete: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                onSignInComplete(task.isSuccessful)
            }
    }
}