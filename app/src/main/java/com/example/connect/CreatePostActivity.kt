package com.example.connect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.connect.daos.PostDao

class CreatePostActivity : AppCompatActivity() {

    private lateinit var postButton : Button
    private lateinit var postInput : EditText
    private lateinit var postDao : PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postButton = findViewById(R.id.postButton)
        postInput = findViewById(R.id.postInput)

        postDao = PostDao()

        postButton.setOnClickListener{
            val input = postInput.text.toString().trim()
            if(input.isNotEmpty()){
                postDao.addPost(input)
                finish()
            }
        }
    }
}