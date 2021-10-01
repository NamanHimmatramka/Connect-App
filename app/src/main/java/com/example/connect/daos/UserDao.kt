package com.example.connect.daos

import com.example.connect.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("users")
    fun addUser(user: User){
        user?.let {
            GlobalScope.launch(Dispatchers.IO){
                usersCollection.document(it.uid).set(it)
            }
        }
    }

    fun getUserById(uid:String): Task<DocumentSnapshot>{
        return usersCollection.document(uid).get();
    }

}