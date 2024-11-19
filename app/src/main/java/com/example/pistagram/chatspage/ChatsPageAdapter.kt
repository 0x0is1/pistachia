package com.example.pistagram.chatspage

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pistagram.R
import com.example.pistagram.chatpage.ChatPageActivity
import com.google.android.material.imageview.ShapeableImageView

class ChatsPageAdapter(private val chats: List<Chats>) : RecyclerView.Adapter<ChatsPageAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById<ShapeableImageView>(R.id.chatItemAvatar)
        val username: TextView = view.findViewById(R.id.chatItemUsername)
        val message: TextView = view.findViewById(R.id.chatItemMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = chats.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]
        Glide.with(holder.itemView.context)
            .load(chat.senders_avatar_url)
            .placeholder(R.drawable.ic_home_profile)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.avatar)
        holder.username.text = chat.username
        holder.message.text = chat.last_message

        if (chat.unread) {
            holder.message.setTypeface(holder.message.typeface, Typeface.BOLD)
            holder.message.typeface = holder.itemView.context.resources.getFont(R.font.kleide_trial_bold)
        } else {
            holder.message.typeface = holder.itemView.context.resources.getFont(R.font.kleide_trial_regular)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatPageActivity::class.java)
            intent.putExtra("senders_username", chat.username)
            holder.itemView.context.startActivity(intent)
        }
    }

}