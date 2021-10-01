package com.example.connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.connect.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(){
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addPost.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_layout, HomeFragment()).commit()

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottomNavigationBar)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_layout,HomeFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.myProfile->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_layout,MyProfileFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logoutButton){
            Firebase.auth.signOut()
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
        }
        return super.onOptionsItemSelected(item)
    }

}