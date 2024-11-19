package com.example.pistagram.profileviewerpage

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pistagram.R
import com.example.pistagram.homepage.PostsComponents.Posts
import com.example.pistagram.homepage.PostsComponents.PostsContainerRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase

class ProfilePostsHandler {
    fun setupProfilePostsHandler(context: Context, view: View, profileUsername: String) {
        val postsRecyclerView = view.findViewById<RecyclerView>(R.id.profilePostsRecyclerView)
        postsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val posts = mutableListOf<Posts>()
        val adapter = PostsContainerRecyclerAdapter(posts)
        postsRecyclerView.adapter = adapter

        val postsReference = FirebaseDatabase.getInstance().getReference("posts").child(profileUsername)
        postsReference.get().addOnSuccessListener { snapshot ->
            for (postSnapshot in snapshot.children) {
                val post = postSnapshot.getValue(Posts::class.java)
                if (post != null) posts.add(post)
                adapter.notifyDataSetChanged()
            }
        }
    }
}