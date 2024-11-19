package com.example.pistagram.chatspage

class Chats(
    val senders_avatar_url: String? = null,
    val senders_username: String? = null,
    val last_message: String? = null,
    val last_message_time: Long? = null,
    var unread: Boolean = false,
    val username: String? = null
)
