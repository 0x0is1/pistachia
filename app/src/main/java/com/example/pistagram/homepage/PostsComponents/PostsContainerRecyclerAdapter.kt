package com.example.pistagram.homepage.PostsComponents

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pistagram.R
import com.example.pistagram.profileviewerpage.ProfileViewerActivity
import com.example.pistagram.utils.CredsEditor
import com.example.pistagram.utils.ImageUri2B64
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostsContainerRecyclerAdapter(private val posts: List<Posts>) : RecyclerView.Adapter<PostsContainerRecyclerAdapter.ViewHolder>() {
    val credsEditor = CredsEditor()
    val imageUri2B64 = ImageUri2B64()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.posterName.text = post.name
        holder.postUsername.text = post.username
        holder.postText.text = post.description
        imageGlider(post.image_url, holder.itemView.context, holder.postImage)
        holder.postLikeCount.text = (post.liked_by?.size ?: 0).toString()
        holder.postCommentsCount.text = (post.commented_by?.size ?: 0).toString()
        holder.postSharesCount.text = (post.shares_count ?: 0).toString()

        val authenticatedUsername = credsEditor.readFileFromInternalStorage(holder.itemView.context, "creds.txt").toString()
        if (authenticatedUsername.isEmpty()) return

        holder.postLike.setOnClickListener { toggleLike(holder, authenticatedUsername, post) }
        holder.postComment.setOnClickListener { onCommentClickListener(holder, authenticatedUsername) }
        holder.postShare.setOnClickListener { onShareClickListener(holder, authenticatedUsername) }
        imageGlider(post.avatar, holder.itemView.context, holder.postUserImage)

        updateLikeIcon(holder, post, authenticatedUsername)
        holder.posterName.setOnClickListener{
            val intent = Intent(holder.itemView.context, ProfileViewerActivity::class.java)
            intent.putExtra("username", post.username)
            holder.itemView.context.startActivity(intent)
        }
    }

    private fun toggleLike(holder: ViewHolder, authenticatedUsername: String, post: Posts) {
        val postId = post.postId ?: return
        val posterName = post.username ?: return

        val postRef = FirebaseDatabase.getInstance().getReference("posts").child(posterName).child(postId).child("liked_by")
        postRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val likedBy = snapshot.children.mapNotNull { it.getValue(String::class.java) }.toMutableList()
                if (likedBy.contains(authenticatedUsername)) {
                    likedBy.remove(authenticatedUsername)
                    holder.postLike.setImageResource(R.drawable.ic_post_like)
                    holder.postLikeCount.text = (holder.postLikeCount.text.toString().toInt() - 1).toString()
                } else {
                    likedBy.add(authenticatedUsername)
                    holder.postLike.setImageResource(R.drawable.ic_post_like_filled)
                    holder.postLikeCount.text = (holder.postLikeCount.text.toString().toInt() + 1).toString()
                }
                postRef.setValue(likedBy)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle potential errors here
            }
        })
    }

    private fun updateLikeIcon(holder: ViewHolder, post: Posts, authenticatedUsername: String) {
        if (post.liked_by?.contains(authenticatedUsername) == true) {
            holder.postLike.setImageResource(R.drawable.ic_post_like_filled)
        } else {
            holder.postLike.setImageResource(R.drawable.ic_post_like)
        }
    }

    private fun onShareClickListener(holder: ViewHolder, authenticatedUsername: String) {
        // Implement sharing functionality here
    }

    private fun onCommentClickListener(holder: ViewHolder, authenticatedUsername: String) {
        // Implement commenting functionality here
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val postUserImage: ImageView = view.findViewById(R.id.post_user_image)
        val posterName: TextView = view.findViewById(R.id.post_user_name)
        val postImage: ImageView = view.findViewById(R.id.post_image)
        val postLike: ImageView = view.findViewById(R.id.post_like)
        val postComment: ImageView = view.findViewById(R.id.post_comment)
        val postShare: ImageView = view.findViewById(R.id.post_share)
        val postText: TextView = view.findViewById(R.id.post_text)
        val postUsername: TextView = view.findViewById(R.id.post_username)
        val postLikeCount: TextView = view.findViewById(R.id.post_like_count)
        val postCommentsCount: TextView = view.findViewById(R.id.post_comments_count)
        val postSharesCount: TextView = view.findViewById(R.id.post_shares_count)
    }

    private fun imageGlider(imageUrl:String?, context: Context, imageViewContainer: ImageView){
        if (imageUrl != null) {
            if(imageUrl.contains("https://")){
                Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_home_profile)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageViewContainer)
            }
            else{
                val decodedImage = imageUri2B64.convertBase64ToBitmap(imageUrl)
                Glide.with(context)
                    .asBitmap()
                    .load(decodedImage)
                    .centerCrop()
                    .placeholder(R.drawable.ic_home_profile)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageViewContainer)
            }
        }
    }
}
