package com.example.pistagram.postviewerpage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pistagram.R
import com.example.pistagram.profileviewerpage.ProfilePostsHandler

class PostViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post_viewer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.post_viewer_page_toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        val username = intent.getStringExtra("username").toString()
        val postId = intent.getStringExtra("postId").toString()
        val postViewerHandler = PostViewerHandler()
        postViewerHandler.setupPostViewerHandler(this, findViewById(R.id.post_page_post_container), username, postId)
    }
}