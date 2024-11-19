package com.example.pistagram.homepage

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pistagram.R
import com.example.pistagram.homepage.NavFooterComponents.NavFooterActions
import com.example.pistagram.homepage.NavHeaderComponents.NavHeaderActions
import com.example.pistagram.homepage.PostsComponents.HomePostsManager
import com.example.pistagram.homepage.StoryComponents.HomeStoriesManager
import com.example.pistagram.utils.CredsEditor

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val credsEditor = CredsEditor()
        credsEditor.writeFileOnInternalStorage(this, "creds.txt", "user1")

        // embed stories container
        val homeStoriesManager = HomeStoriesManager()
        homeStoriesManager.embedStoriesContainer(this)

        // embed posts container
        val homePostsManager = HomePostsManager()
        homePostsManager.embedPostsContainer(this)

        // set footer action listener
        val footerView = findViewById<View>(R.id.footer_include)
        val navFooterActions = NavFooterActions()
        navFooterActions.setupFooterActions(footerView, this)

        // set header action listener
        val headerView = findViewById<View>(R.id.header_include)
        val navHeaderActions = NavHeaderActions()
        navHeaderActions.setupHeaderActions(headerView, this)
    }
}