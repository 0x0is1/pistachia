package com.example.pistagram.profileviewerpage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.pistagram.R
import com.example.pistagram.chatpage.ChatPageActivity
import com.example.pistagram.profilepage.Profile
import com.example.pistagram.utils.CredsEditor
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class ProfileDetailsHandler {
    @SuppressLint("SetTextI18n")
    fun setupProfileDetailsHandler(context: Context, view: View, lifecycleOwner: LifecycleOwner, profileUsername: String) {
        val userAvatar = view.findViewById<ShapeableImageView>(R.id.userAvatar)
        val name = view.findViewById<TextView>(R.id.name)
        val username = view.findViewById<TextView>(R.id.username)
        val postsCount = view.findViewById<TextView>(R.id.postsCount)
        val followersCount = view.findViewById<TextView>(R.id.followersCount)
        val followingCount = view.findViewById<TextView>(R.id.followingCount)
        val bio = view.findViewById<TextView>(R.id.bio)
        val messageBtn = view.findViewById<TextView>(R.id.messageBtn)
        val followBtn = view.findViewById<TextView>(R.id.followBtn)
        val credsEditor = CredsEditor()
        val authenticatedUsername = credsEditor.readFileFromInternalStorage(context, "creds.txt").toString()
        val profileReference = FirebaseDatabase.getInstance().getReference("profiles").child(profileUsername)
        followBtn.setOnClickListener{
            profileReference.get().addOnSuccessListener {
                val profile = it.getValue(Profile::class.java)
                if (profile != null) {
                    if (profile.followers_list?.contains(authenticatedUsername) == true) {
                        profile.followers_list.remove(authenticatedUsername)
                        profile.following_list?.remove(profileUsername)
                        profileReference.setValue(profile)
                        followBtn.text = "Follow"
                        followBtn.backgroundTintList = context.getColorStateList(R.color.tertiaryColor)
                        followBtn.setTextColor(context.getColor(R.color.quadrataryColor))
                        followBtn.isEnabled = true
                    }
                    else {
                        profile.followers_list?.add(authenticatedUsername)
                        profile.following_list?.add(profileUsername)
                        profileReference.setValue(profile)
                        followBtn.text = "Following"
                        followBtn.backgroundTintList = context.getColorStateList(R.color.black)
                        followBtn.setTextColor(context.getColor(R.color.primaryBackgroundColor))
                        followBtn.isEnabled = false
                    }
                }
            }
        }
        messageBtn.setOnClickListener {
            val intent = Intent(context, ChatPageActivity::class.java)
            intent.putExtra("senders_username", profileUsername)
            context.startActivity(intent)
        }
        lifecycleOwner.lifecycleScope.launch {
            profileReference.addValueEventListener(object : ValueEventListener {
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
                        if (profile.followers_list?.contains(authenticatedUsername) == true){
                            followBtn.text = "Following"
                            followBtn.backgroundTintList = context.getColorStateList(R.color.black)
                            followBtn.setTextColor(context.getColor(R.color.primaryBackgroundColor))
                            followBtn.isEnabled = false
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
        }

    }

}