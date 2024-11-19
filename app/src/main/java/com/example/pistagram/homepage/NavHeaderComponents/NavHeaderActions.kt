package com.example.pistagram.homepage.NavHeaderComponents

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.example.pistagram.R
import com.example.pistagram.chatspage.ChatsPageActivity

class NavHeaderActions {
    fun setupHeaderActions(headerView: View, context: Context) {
        val messagesButton = headerView.findViewById<ImageView>(R.id.messagesButton)
        messagesButton.setOnClickListener {
            val intent = Intent(context, ChatsPageActivity::class.java)
            context.startActivity(intent)
        }
    }
}