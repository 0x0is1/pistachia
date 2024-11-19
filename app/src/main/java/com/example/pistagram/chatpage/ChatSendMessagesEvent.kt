package com.example.pistagram.chatpage

import android.widget.Button
import android.widget.EditText
import com.example.pistagram.chatspage.Chats
import com.google.firebase.database.FirebaseDatabase

class ChatSendMessagesEvent {
    fun setupMessageSentHandler(messageEdt:EditText, sendButton:Button, sendersUsername:String, sendersAvatarUrl:String, receiversUsername:String){
        val database = FirebaseDatabase.getInstance()
        val chatsReference = database.getReference("chats")
        val conversationsReference = database.getReference("conversations")
        sendButton.setOnClickListener {
            val message = messageEdt.text.toString()
            val time = System.currentTimeMillis()
            val convoOwnerSend =  conversationsReference.child(sendersUsername).child(receiversUsername).key
            val convoOwnerReceive =  conversationsReference.child(receiversUsername).child(sendersUsername).key
            val conversation = Chat(sendersUsername, sendersAvatarUrl, message, time)
            val lastConvoSend = Chats(sendersAvatarUrl, sendersUsername, message, time, false, convoOwnerSend)
            val lastConvoReceive = Chats(sendersAvatarUrl, sendersUsername, message, time, false, convoOwnerReceive)
            chatsReference.child(sendersUsername).child(receiversUsername).child("messages").push().setValue(conversation)
            chatsReference.child(receiversUsername).child(sendersUsername).child("messages").push().setValue(conversation)
            conversationsReference.child(sendersUsername).child(receiversUsername).setValue(lastConvoSend)
            lastConvoReceive.unread = true
            conversationsReference.child(receiversUsername).child(sendersUsername).setValue(lastConvoReceive)
            messageEdt.text.clear()
        }
    }
}