// ChatActivity.kt
package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ChatActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val messages = mutableListOf<Message>()
    private lateinit var messageAdapter: MessageAdapter

    private lateinit var sellerNickname: String
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // 채팅 상대방의 닉네임 가져오기
        sellerNickname = intent.getStringExtra("sellerNickname") ?: ""
        title = sellerNickname // 액션바에 상대방 닉네임 표시

        // Initialize RecyclerView and set up layout manager
        recyclerView = findViewById(R.id.recyclerView) // 추가된 부분
        messageAdapter = MessageAdapter(this, messages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter

        // Load messages from Firestore
        loadMessages()

        val sendButton = findViewById<Button>(R.id.sendButton)
        val messageEditText = findViewById<EditText>(R.id.messageEditText)

        sendButton.setOnClickListener {
            val sender = "User" // Replace with actual user identification
            val content = messageEditText.text.toString().trim()

            if (content.isNotEmpty()) {
                val message = Message(sender, content, System.currentTimeMillis())
                messageAdapter.addMessage(message)
                saveMessage(message) // Firestore에 메시지 저장 로직 추가
                messageEditText.text.clear()
            }
        }

    }


    private fun loadMessages() {
        // Use Firestore query to load messages
        db.collection("messages")
            .whereEqualTo("sender", "User") // Filter messages by sender (User)
            .whereEqualTo("recipient", sellerNickname) // Filter messages by recipient (Seller)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                messages.clear()

                for (document in snapshot!!.documents) {
                    val sender = document.getString("sender") ?: ""
                    val content = document.getString("content") ?: ""
                    val timestamp = document.getLong("timestamp") ?: 0

                    val message = Message(sender, content, timestamp)
                    messages.add(message)
                }

                // Notify adapter after loading messages
                messageAdapter.notifyDataSetChanged()
            }
    }

    private fun saveMessage(message: Message) {
        // Save a message to Firestore
        db.collection("messages")
            .add(message)
            .addOnSuccessListener {
                // Handle success, if needed
            }
            .addOnFailureListener {
                // Handle failure, if needed
            }
    }
}
