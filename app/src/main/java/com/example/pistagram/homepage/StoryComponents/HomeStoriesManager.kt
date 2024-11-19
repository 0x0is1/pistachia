package com.example.pistagram.homepage.StoryComponents

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pistagram.R
import com.example.pistagram.homepage.HomePageActivity
import com.example.pistagram.utils.CredsEditor
import com.google.firebase.database.FirebaseDatabase

class HomeStoriesManager {
    fun embedStoriesContainer(view: HomePageActivity){
        // set up story recycler view
        val recyclerView = view.findViewById<RecyclerView>(R.id.stories_container)
        recyclerView.layoutManager = LinearLayoutManager(view, LinearLayoutManager.HORIZONTAL, false)
        val storyList = mutableListOf<Story>()
        val adapter = StoriesContainerRecyclerAdapter(storyList)
        recyclerView.adapter = adapter

        val credsEditor = CredsEditor()
        val username = credsEditor.readFileFromInternalStorage(view, "creds.txt").toString()
        val followingReference = FirebaseDatabase.getInstance().getReference("profiles").child(username).child("following_list")
        val storiesRef = FirebaseDatabase.getInstance().getReference("stories")
        followingReference.get().addOnSuccessListener { snapshot ->
            for (usernameSnapshot in snapshot.children) {
                val user_name = usernameSnapshot.value.toString()
                storiesRef.child(user_name).get().addOnSuccessListener { storySnapshot ->
                    for (storySnapshot in storySnapshot.children) {
                        val story = Story(
                            storySnapshot.child("storyId").value.toString(),
                            storySnapshot.child("avatar").value.toString(),
                            storySnapshot.child("username").value.toString()
                        )
                        storyList.add(story)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}