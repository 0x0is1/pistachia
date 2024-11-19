package com.example.pistagram.profileviewerpage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pistagram.R

class ProfileViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_viewer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val username = intent.getStringExtra("username").toString()
        val toolbar = findViewById<Toolbar>(R.id.profile_page_toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.title = username

        val profileDetailsHandler = ProfileDetailsHandler()
        profileDetailsHandler.setupProfileDetailsHandler(this, findViewById(R.id.profileDetailInclude), this, username)

        val profilePostsHandler = ProfilePostsHandler()
        profilePostsHandler.setupProfilePostsHandler(this, findViewById(R.id.profilePostsRecyclerView), username)
    }
}