package com.example.connect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.connect.daos.PostDao
import com.example.connect.daos.UserDao
import com.example.connect.databinding.FragmentMyProfileBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyProfileFragment : Fragment(), IMyProfilePostAdapter {
    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var adapter: MyProfilePostAdapter
    private lateinit var postDao: PostDao
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(layoutInflater)
        postDao = PostDao()
        userDao = UserDao()
        setUpRecyclerView()
        setUserData()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setUserData() {
        Glide.with(binding.profilePicture.context).load(Firebase.auth.currentUser!!.photoUrl).circleCrop().into(binding.profilePicture)
        binding.userName.text = Firebase.auth.currentUser!!.displayName
    }

    private fun setUpRecyclerView() {
        val postsCollection = postDao.postCollection
        val query = postsCollection.whereEqualTo("createdBy.uid" , Firebase.auth.currentUser!!.uid)
        val options =  FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = MyProfilePostAdapter(options, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.recyclerView.context)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDeleteButtonClicked(postId: String) {
        postDao.removePostById(postId)
    }
}