package com.example.connect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class MyProfilePostAdapter(options: FirestoreRecyclerOptions<Post>, private val listener: IMyProfilePostAdapter) :
    FirestoreRecyclerAdapter<Post, MyProfilePostViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProfilePostViewHolder {
        val viewHolder = MyProfilePostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.myprofile_item_post, parent, false))
        viewHolder.deleteButton.setOnClickListener {
            listener.onDeleteButtonClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyProfilePostViewHolder, position: Int, model: Post) {
        holder.postText.text = model.text
        holder.userName.text = model.createdBy.displayName
        holder.likeCount.text = model.likedBy.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)
    }
}

class MyProfilePostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val postText: TextView = itemView.findViewById(R.id.postTitle)
    val userName: TextView = itemView.findViewById(R.id.userName)
    val userImage: ImageView = itemView.findViewById(R.id.userImage)
    val likeCount: TextView = itemView.findViewById(R.id.likeCount)
    val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    val createdAt: TextView = itemView.findViewById(R.id.timeStamp)
}

interface IMyProfilePostAdapter{
    fun onDeleteButtonClicked(postId: String)
}