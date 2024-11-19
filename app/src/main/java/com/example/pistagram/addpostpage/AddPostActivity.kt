package com.example.pistagram.addpostpage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.pistagram.R
import com.example.pistagram.homepage.PostsComponents.Posts
import com.example.pistagram.utils.CredsEditor
import com.example.pistagram.utils.ImageUri2B64
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class AddPostActivity : AppCompatActivity() {

    private lateinit var pickerButton: ImageView
    private lateinit var sendButton: ImageView
    private lateinit var selectedImageUri: Uri

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    setImageOnPickerButton(selectedImageUri)
                    this.selectedImageUri = selectedImageUri
                } else {
                    this.selectedImageUri = Uri.EMPTY
                    Toast.makeText(this, "Failed to select image", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.chat_toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        val credsEditor = CredsEditor()
        val username = credsEditor.readFileFromInternalStorage(this, "creds.txt").toString()
        pickerButton = findViewById(R.id.pickerButtonImage)
        sendButton = findViewById(R.id.sendButton)
        pickerButton.setOnClickListener { openImagePicker() }
        val spinner = findViewById<Spinner>(R.id.post_type)
        val profileRef = FirebaseDatabase.getInstance().getReference("profiles").child(username)

        profileRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val avatar_url = snapshot.child("avatar_url").value.toString()
                val name = snapshot.child("name").value.toString()
                val user_name = snapshot.child("username").value.toString()

                sendButton.setOnClickListener {
                    val postType = spinner.selectedItemPosition
                    val description = findViewById<TextInputEditText>(R.id.descriptionInput).text.toString()
                    val imageBase64Uri = ImageUri2B64().convertImageToBase64(this, selectedImageUri)
                    val postId = UUID.randomUUID().toString()
                    val liked_by = ArrayList<String>()
                    val commented_by = ArrayList<String>()
                    val shares_count = 0

                    if (postType == 0) {
                        val post = Posts(postId, name, user_name, description, imageBase64Uri, avatar_url, liked_by, commented_by, shares_count)
                        val postsRef = FirebaseDatabase.getInstance().getReference("posts").child(user_name)
                        postsRef.child(postId).setValue(post)
                        Toast.makeText(this, "Post created successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val time = System.currentTimeMillis()
                        val story = StoryPost(postId, user_name, description, imageBase64Uri, avatar_url, time)
                        val storiesRef = FirebaseDatabase.getInstance().getReference("stories").child(user_name)
                        storiesRef.child(postId).setValue(story)
                        Toast.makeText(this, "Story created successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Profile not found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
        }

    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun setImageOnPickerButton(imageUri: Uri) {
        try {
            pickerButton.setImageURI(imageUri)
            pickerButton.updatePadding(0, 0, 0, 0)
            sendButton.isClickable = true
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error setting image", Toast.LENGTH_SHORT).show()
        }
    }
}
