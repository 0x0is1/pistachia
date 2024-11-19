package com.example.pistagram.chatspage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pistagram.R
import com.example.pistagram.utils.CredsEditor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class ChatsPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chats_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar = findViewById<Toolbar>(R.id.chats_message_toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.chats_recycler_container)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val chats = mutableListOf<Chats>()
        val adapter = ChatsPageAdapter(chats)
        recyclerView.adapter = adapter

        val database = FirebaseDatabase.getInstance()
        val credsEditor = CredsEditor()
        val authorizedUsername = credsEditor.readFileFromInternalStorage(this, "creds.txt")
        if (authorizedUsername.isNullOrEmpty()) return
        val conversationsRef = database.getReference("conversations").child(authorizedUsername)
        this.lifecycleScope.launch {
            conversationsRef.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    chats.clear()
                    for (userSnapshot in snapshot.children) {
                        val conversation = userSnapshot.getValue(Chats::class.java)
                        if (conversation != null) chats.add(conversation)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
}
 