package com.example.pistagram.homepage.StoryComponents

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
import com.example.pistagram.storypage.StoryPageActivity
import com.example.pistagram.utils.ImageUri2B64
import com.google.android.material.imageview.ShapeableImageView

class StoriesContainerRecyclerAdapter(private val storyList: List<Story>) : RecyclerView.Adapter<StoriesContainerRecyclerAdapter.ViewHolder>() {
    val imageUri2B64 = ImageUri2B64()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.story_indicator, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val story = storyList[position]
        imageGlider(story.storyUserUrl, holder.itemView.context, holder.storyUserImage)
        holder.storyUserName.text = story.storyUserName
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, StoryPageActivity::class.java)
            intent.putExtra("username", story.storyUserName)
            intent.putExtra("storyId", story.storyId)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return storyList.size
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storyUserImage: ShapeableImageView = view.findViewById(R.id.story_user_image)
        val storyUserName: TextView = view.findViewById(R.id.story_user_name)
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