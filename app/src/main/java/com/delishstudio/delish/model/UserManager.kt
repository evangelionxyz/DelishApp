package com.delishstudio.delish.model

import com.delishstudio.delish.system.DatabaseStrRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserManager {
    companion object {
        var Main: UserModel = UserModel("", "", "", "")

        fun Update() {
            val firebaseRef = FirebaseDatabase.getInstance().getReference(DatabaseStrRef.USERS)
            val query = firebaseRef.orderByChild(DatabaseStrRef.EMAIL).equalTo(Main.email)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (userSnapshot in dataSnapshot.children) {
                        val userKey = userSnapshot.key
                        userKey?.let {
                            firebaseRef.child(it).setValue(Main)
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }

        fun Available(callback: (Boolean) -> Unit) = if (Main.userName!!.isEmpty())
        {
            val email = FirebaseAuth.getInstance().currentUser!!.email.toString()
            val userListRef = FirebaseDatabase.getInstance().getReference(DatabaseStrRef.USERS)
            val query = userListRef.orderByChild(DatabaseStrRef.EMAIL).equalTo(email)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var userFound = false
                    for (userSnapshot in dataSnapshot.children)
                    {
                        val user = userSnapshot.getValue(UserModel::class.java)
                        if (user != null) {
                            Main = user
                            userFound = true
                        }
                    }
                    callback(userFound)
                }
                override fun onCancelled(error: DatabaseError) = callback(false)
            })
        }
        else callback(true)
    }
}