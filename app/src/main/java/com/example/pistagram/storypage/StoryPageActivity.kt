package com.example.pistagram.storypage

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.pistagram.R
import com.example.pistagram.utils.ImageUri2B64
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.Date

class StoryPageActivity : AppCompatActivity() {
    private val imageUri2B64 = ImageUri2B64()
    private val timeoutDuration = 10_000L
    private var timeoutJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_story_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.story_page_toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val username = intent.getStringExtra("username").toString()
        val storyId = intent.getStringExtra("storyId").toString()
        val storyRef = FirebaseDatabase.getInstance().getReference("stories")
            .child(username)
            .child(storyId)

        storyRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val image_url = snapshot.child("image_url").value.toString()
                val description = snapshot.child("description").value.toString()
                val user_name = snapshot.child("username").value.toString()
                val sdf = SimpleDateFormat("HH:mm")
                val time = Date(snapshot.child("timestamp").value.toString().toLong())
                val timeString = sdf.format(time)

                val storyImage = findViewById<ImageView>(R.id.storyImage)
                val storyDescription = findViewById<TextView>(R.id.storyDescription)
                toolbar.title = "@$user_name • $timeString"
                storyDescription.text = description
                imageGlider(image_url, this, storyImage)
            }
        }

        startTimeout()
    }

    private fun startTimeout() {
        timeoutJob = CoroutineScope(Dispatchers.Main).launch {
            delay(timeoutDuration)
            finish()
        }
    }

    private fun imageGlider(imageUrl: String?, context: Context, imageViewContainer: ImageView) {
        if (imageUrl != null) {
            if (imageUrl.contains("https://")) {
                Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_home_profile)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageViewContainer)
            } else {
                val decodedImage = imageUri2B64.convertBase64ToBitmap(imageUrl)
                Glide.with(context)
                    .asBitmap()
                    .load(decodedImage)
                    .centerCrop()
                    .placeholder(R.drawable.ic_home_profile)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageViewContainer)
            }
        }
    }

    override fun onDestroy() {
        timeoutJob?.cancel()
        super.onDestroy()
    }
}
