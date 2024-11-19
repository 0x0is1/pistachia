package com.example.pistagram.chatpage

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pistagram.R
import java.text.SimpleDateFormat
import java.util.Date

class ChatPageAdapter(private val conversation: List<Chat>, private val authenticatedUsername: String) : RecyclerView.Adapter<ChatPageAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.chatItemAvatar)
        val username: TextView = view.findViewById(R.id.chatItemUsername)
        val message: TextView = view.findViewById(R.id.chatItemMessage)
        val time: TextView = view.findViewById(R.id.chatItemTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_chat_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = conversation.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = conversation[position]
        Glide.with(holder.itemView.context)
            .load(chat.senders_avatar_url)
            .placeholder(R.drawable.ic_home_profile)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.avatar)
        holder.username.text = chat.senders_username
        holder.message.text = chat.message
        holder.time.text = timestamp2time(chat.time ?: 0)

        if (chat.senders_username == authenticatedUsername) {
            holder.username.visibility = View.GONE
            holder.avatar.visibility = View.GONE
            holder.message.background = AppCompatResources.getDrawable(holder.itemView.context, R.drawable.self_message_bg)
            holder.message.setTextColor(holder.itemView.context.resources.getColor(R.color.primaryBackgroundColor))
            holder.message.gravity = Gravity.END
            holder.itemView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        } else {
            holder.username.visibility = View.VISIBLE
            holder.avatar.visibility = View.VISIBLE
            holder.message.background = AppCompatResources.getDrawable(holder.itemView.context, R.drawable.reel_trackname_bg)
            holder.message.setTextColor(holder.itemView.context.resources.getColor(R.color.quadrataryColor))
            holder.message.gravity = Gravity.START
            holder.itemView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
    }
    private fun timestamp2time(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }

}