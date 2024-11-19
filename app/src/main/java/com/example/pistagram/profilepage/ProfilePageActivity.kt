package com.example.pistagram.profilepage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pistagram.R

class ProfilePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar = findViewById<Toolbar>(R.id.self_profile_page_toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val userDetailsHandler = UserDetailsHandler()
        userDetailsHandler.setupUserDetailsHandler(this, findViewById(R.id.selfProfileDetailInclude), this)

        val postsHandler = PostsHandler()
        postsHandler.setupPostsHandler(this, findViewById(R.id.profilePostsRecyclerView))
    }
}