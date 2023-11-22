// ChatActivity.kt
package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val messages = mutableListOf<Message>()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var nickname: String

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        firestore = FirebaseFirestore.getInstance()

        // 채팅 상대방의 닉네임 가져오기
        //sellerNickname = intent.getStringExtra("sellerNickname") ?: ""
        //title = sellerNickname // 액션바에 상대방 닉네임 표시

        messageAdapter = MessageAdapter(this, messages)

        // Load messages from Firestore
        loadMessages()

        val user = Firebase.auth.currentUser
        val userId = user?.uid ?: ""
        val sellerNickname = intent.getStringExtra("sellerNickname")
        val productTitle = intent.getStringExtra("productTitle")

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    nickname = document.getString("nickname") ?: ""
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "닉네임 불러오기 실패: $exception", Toast.LENGTH_SHORT).show()
            }

        val sendButton = findViewById<AppCompatImageButton>(R.id.sendButton)
        val messageEditText = findViewById<EditText>(R.id.messageEditText)
        val TotextView = findViewById<TextView>(R.id.TotextView)
        TotextView.text = "${sellerNickname}님께 메세지 보내기"

        sendButton.setOnClickListener {
            val sender = nickname // Replace with actual user identification
            val content = messageEditText.text.toString().trim()
            val receiver = sellerNickname.toString()
            val product =productTitle.toString()

            if (content.isNotEmpty()) {
                val message = Message(sender, receiver, product, content, System.currentTimeMillis())
                //messageAdapter.addMessage(message)
                saveMessage(message) // Firestore에 메시지 저장 로직 추가
                messageEditText.text.clear()
            }
        }

    }


    private fun loadMessages() {
        // Use Firestore query to load messages
        db.collection("messages")
            .whereEqualTo("sender", "User") // Filter messages by sender (User)
            //.whereEqualTo("recipient", sellerNickname) // Filter messages by recipient (Seller)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                messages.clear()

                for (document in snapshot!!.documents) {
                    val sender = document.getString("sender") ?: ""
                    val receiver = document.getString("receiver") ?: ""
                    val product = document.getString("product") ?: ""
                    val content = document.getString("content") ?: ""
                    val timestamp = document.getLong("timestamp") ?: 0

                    val message = Message(sender, receiver, product, content, timestamp)
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
