package com.example.pistagram.profilepage

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pistagram.R
import com.example.pistagram.homepage.PostsComponents.Posts
import com.example.pistagram.homepage.PostsComponents.PostsContainerRecyclerAdapter
import com.example.pistagram.utils.CredsEditor
import com.google.firebase.database.FirebaseDatabase

class PostsHandler {
    fun setupPostsHandler(context: Context, view: View){
        val postsRecyclerView = view.findViewById<RecyclerView>(R.id.profilePostsRecyclerView)
        postsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val posts = mutableListOf<Posts>()
        val adapter = PostsContainerRecyclerAdapter(posts)
        postsRecyclerView.adapter = adapter

        val credsEditor = CredsEditor()
        val authenticatedUsername = credsEditor.readFileFromInternalStorage(context, "creds.txt").toString()
        val postsReference = FirebaseDatabase.getInstance().getReference("posts").child(authenticatedUsername)
        postsReference.get().addOnSuccessListener { snapshot ->
            for (postSnapshot in snapshot.children) {
                val post = postSnapshot.getValue(Posts::class.java)
                if (post != null) posts.add(post)
                adapter.notifyDataSetChanged()
            }
        }
    }
}