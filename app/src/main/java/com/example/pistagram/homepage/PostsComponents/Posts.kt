package com.example.pistagram.homepage.PostsComponents

data class Posts(
    val postId: String? = null,
    val name: String? = null,
    val username: String? = null,
    val description: String? = null,
    val image_url: String? = null,
    val avatar: String? = null,
    val liked_by: List<String>? = null,
    val commented_by: List<String>? = null,
    val shares_count: Int? = 0
)
