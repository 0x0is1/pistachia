package com.example.pistagram.profilepage

class Profile(
    val avatar_url: String? = null,
    val name: String? = null,
    val username: String? = null,
    val posts_count: Int? = null,
    val followers_list: MutableList<String>? = null,
    val following_list: MutableList<String>? = null,
    val bio: String? = null
)