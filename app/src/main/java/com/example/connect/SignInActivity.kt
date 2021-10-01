 package com.example.connect

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.connect.R
import com.example.connect.daos.UserDao
import com.example.connect.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

 class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        auth = Firebase.auth

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        binding.signInButton.setOnClickListener {
            resultLauncher.launch(signInIntent)
        }
    }

     override fun onStart() {
         super.onStart()
         val currentUser = auth.currentUser
         updateUI(currentUser)
     }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult() ,
        ActivityResultCallback { result->
            if(result.resultCode == Activity.RESULT_OK){
                val intent = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                handleSignInResult(task)
            }
        })

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try{
            val account = task?.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        }
        catch (e: ApiException){
            Log.w(TAG, "Google sign in Failed" , e)
        }
    }

     private fun firebaseAuthWithGoogle(idToken: String) {
         val credential = GoogleAuthProvider.getCredential(idToken, null)
         binding.signInButton.visibility = View.GONE
         binding.progressBar.visibility = View.VISIBLE
         GlobalScope.launch(Dispatchers.IO){
             val auth = auth.signInWithCredential(credential).await()
             val firebaseUser = auth.user
             withContext(Dispatchers.Main){
                 updateUI(firebaseUser)
             }
         }

     }

     private fun updateUI(firebaseUser: FirebaseUser?) {
         if(firebaseUser != null){

             val user = User(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())
             val userDao = UserDao()
             userDao.addUser(user)

             val mainActivityIntent = Intent(this, MainActivity::class.java)
             startActivity(mainActivityIntent)
             finish()
         }
         else{
             binding.signInButton.visibility = View.VISIBLE
             binding.progressBar.visibility = View.GONE
         }
     }

}