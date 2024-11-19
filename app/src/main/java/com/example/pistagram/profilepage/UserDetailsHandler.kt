package com.example.pistagram.profilepage

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.pistagram.R
import com.example.pistagram.utils.CredsEditor
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class UserDetailsHandler {
    fun setupUserDetailsHandler(context: Context, view: View, lifecycleOwner: LifecycleOwner){
        val userAvatar = view.findViewById<ShapeableImageView>(R.id.userAvatar)
        val name = view.findViewById<TextView>(R.id.name)
        val username = view.findViewById<TextView>(R.id.username)
        val postsCount = view.findViewById<TextView>(R.id.postsCount)
        val followersCount = view.findViewById<TextView>(R.id.followersCount)
        val followingCount = view.findViewById<TextView>(R.id.followingCount)
        val bio = view.findViewById<TextView>(R.id.bio)
        val editProfile = view.findViewById<TextView>(R.id.editBtn)
        editProfile.setOnClickListener {
            Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
        val credsEditor = CredsEditor()
        val authenticatedUsername = credsEditor.readFileFromInternalStorage(context, "creds.txt").toString()
        val profileReference = FirebaseDatabase.getInstance().getReference("profiles").child(authenticatedUsername)
        lifecycleOwner.lifecycleScope.launch {
            profileReference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profile = snapshot.getValue(Profile::class.java)
                    if (profile != null){
                        name.text = profile.name
                        username.text = profile.username
                        postsCount.text = profile.posts_count.toString()
                        followersCount.text = (profile.followers_list?.size?:0).toString()
                        followingCount.text = (profile.following_list?.size?:0).toString()
                        bio.text = profile.bio
                        Glide.with(context)
                            .load(profile.avatar_url)
                            .placeholder(R.drawable.ic_home_profile)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(userAvatar)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
        }

    }
}