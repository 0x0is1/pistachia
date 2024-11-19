package com.example.pistagram.postviewerpage

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pistagram.homepage.PostsComponents.Posts
import com.example.pistagram.homepage.PostsComponents.PostsContainerRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase

class PostViewerHandler {
    fun setupPostViewerHandler(activity: Activity, recyclerView: RecyclerView, username: String, postId: String) {
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val posts = mutableListOf<Posts>()
        val adapter = PostsContainerRecyclerAdapter(posts)
        recyclerView.adapter = adapter

        val postsReference = FirebaseDatabase.getInstance().getReference("posts").child(username)
        postsReference.get().addOnSuccessListener { snapshot ->
            for (postSnapshot in snapshot.children) {
                val post = postSnapshot.getValue(Posts::class.java)
                if (post != null && post.postId == postId) posts.add(post)
                adapter.notifyDataSetChanged()
            }
        }
    }
}