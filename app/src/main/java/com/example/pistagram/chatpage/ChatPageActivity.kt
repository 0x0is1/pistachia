package com.example.pistagram.chatpage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pistagram.R
import com.example.pistagram.profileviewerpage.ProfileViewerActivity
import com.example.pistagram.utils.CredsEditor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class ChatPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sendersUsername = intent.getStringExtra("senders_username").toString()
        val toolbar = findViewById<Toolbar>(R.id.chat_toolbar)
        toolbar.title = sendersUsername
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.setOnClickListener{
            val intent = Intent(this, ProfileViewerActivity::class.java)
            intent.putExtra("username", sendersUsername)
            startActivity(intent)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.chat_convo_container)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        layoutManager.setStackFromEnd(true);
        recyclerView.layoutManager = layoutManager

        val credsEditor = CredsEditor()
        val authenticatedUsername = credsEditor.readFileFromInternalStorage(this, "creds.txt")
        if (authenticatedUsername.isNullOrEmpty()) return
        val conversation = mutableListOf<Chat>()
        val adapter = ChatPageAdapter(conversation, authenticatedUsername)
        recyclerView.adapter = adapter

        val database = FirebaseDatabase.getInstance()
        val conversationHistoryReference = database.getReference("chats").child(authenticatedUsername).child(sendersUsername).child("messages")
        val selfProfileReference = database.getReference("profiles").child(authenticatedUsername)
        val conversationsReference = database.getReference("conversations")
        if (authenticatedUsername != sendersUsername) conversationsReference.child(authenticatedUsername).child(sendersUsername).child("unread").setValue(false)
        this.lifecycleScope.launch {
            conversationHistoryReference.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    conversation.clear()
                    for (chatSnapshot in snapshot.children) {
                        val chat = chatSnapshot.getValue(Chat::class.java)
                        if (chat != null) conversation.add(chat)
                    }
                    adapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

        val messageEdt = findViewById<EditText>(R.id.chat_send_input)
        val sendButton = findViewById<Button>(R.id.chat_send_btn)
        val chatSendMessagesEvent = ChatSendMessagesEvent()
        selfProfileReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val selfAvatarUrl = snapshot.child("avatar_url").value.toString()
                val selfUsername = snapshot.child("username").value.toString()
                chatSendMessagesEvent.setupMessageSentHandler(messageEdt, sendButton, selfUsername, selfAvatarUrl, sendersUsername)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}