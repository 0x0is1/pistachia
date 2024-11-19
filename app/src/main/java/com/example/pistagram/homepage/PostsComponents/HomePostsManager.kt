package com.example.pistagram.homepage.PostsComponents

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pistagram.R
import com.example.pistagram.homepage.HomePageActivity
import com.example.pistagram.utils.CredsEditor
import com.google.firebase.database.FirebaseDatabase

class HomePostsManager {
    fun embedPostsContainer(view: HomePageActivity) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.posts_container)
        recyclerView.layoutManager = LinearLayoutManager(view, LinearLayoutManager.VERTICAL, false)
        val posts = mutableListOf<Posts>()
        val adapter = PostsContainerRecyclerAdapter(posts)
        recyclerView.adapter = adapter

        val credsEditor = CredsEditor()
        val authenticatedUsername = credsEditor.readFileFromInternalStorage(view, "creds.txt").toString()
        val followingReference = FirebaseDatabase.getInstance().getReference("profiles").child(authenticatedUsername).child("following_list")
        val postsReference = FirebaseDatabase.getInstance().getReference("posts")
        followingReference.get().addOnSuccessListener { snapshot ->
            for (usernameSnapshot in snapshot.children) {
                val username = usernameSnapshot.value.toString()
                postsReference.child(username).get().addOnSuccessListener { postsSnapshot ->
                    for (postSnapshot in postsSnapshot.children) {
                        val post = postSnapshot.getValue(Posts::class.java)
                        if (post != null) posts.add(post)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}
