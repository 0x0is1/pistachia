package com.example.pistagram.searchpage

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pistagram.R
import com.google.firebase.database.FirebaseDatabase

class SearchPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        val searchBox = findViewById<EditText>(R.id.searchBox)
        val searchRecyclerView = findViewById<RecyclerView>(R.id.search_recycler_view)
        val searchList = mutableListOf<SearchItem>()
        searchRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchRecyclerView.adapter = SearchRecyclerAdapter(searchList)
        searchBox.addTextChangedListener {
            val searchText = it.toString()
            val profilesRef = FirebaseDatabase.getInstance().getReference("profiles")
            profilesRef.get().addOnSuccessListener { snapshot ->
                searchList.clear()
                for (profile in snapshot.children) {
                    val username = profile.child("username").value.toString()
                    val name = profile.child("name").value.toString()
                    if (username.contains(searchText) || name.contains(searchText)) {
                        val avatar = profile.child("avatar_url").value.toString()
                        val name = profile.child("name").value.toString()
                        val user_name = profile.child("username").value.toString()
                        searchList.add(SearchItem(avatar, name, user_name))
                    }
                }
                searchRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }
}