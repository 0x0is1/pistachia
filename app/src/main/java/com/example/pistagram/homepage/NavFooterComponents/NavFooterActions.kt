package com.example.pistagram.homepage.NavFooterComponents

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.example.pistagram.R
import com.example.pistagram.addpostpage.AddPostActivity
import com.example.pistagram.homepage.HomePageActivity
import com.example.pistagram.profilepage.ProfilePageActivity
import com.example.pistagram.reelpage.ReelActivity
import com.example.pistagram.searchpage.SearchPageActivity

class NavFooterActions {
    fun setupFooterActions(footerView: View, context: Context) {
        val homeButton = footerView.findViewById<ImageView>(R.id.footerHomeButton)
        val searchButton = footerView.findViewById<ImageView>(R.id.footerSearchButton)
        val addButton = footerView.findViewById<ImageView>(R.id.footerAddButton)
        val footerReelsButton = footerView.findViewById<ImageView>(R.id.footerReelButton)
        val profileButton = footerView.findViewById<ImageView>(R.id.footerProfileButton)

        homeButton.setOnClickListener {
            val intent = Intent(context, HomePageActivity::class.java)
            context.startActivity(intent)
        }
        searchButton.setOnClickListener {
            val intent = Intent(context, SearchPageActivity::class.java)
            context.startActivity(intent)
        }
        addButton.setOnClickListener {
            val intent = Intent(context, AddPostActivity::class.java)
            context.startActivity(intent)
        }
        footerReelsButton.setOnClickListener {
            val intent = Intent(context, ReelActivity::class.java)
            context.startActivity(intent)
        }
        profileButton.setOnClickListener {
            val intent = Intent(context, ProfilePageActivity::class.java)
            context.startActivity(intent)
        }
    }
}
