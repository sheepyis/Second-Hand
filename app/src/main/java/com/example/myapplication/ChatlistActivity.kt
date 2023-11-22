package com.example.myapplication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class ChatlistActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerview: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatlist)
        recyclerview = findViewById(R.id.recyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)

        firestore = Firebase.firestore

        // Firestore에서 메시지 데이터 가져오기
        firestore.collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val messagesList = mutableListOf<Message>()

                for (document in querySnapshot) {
                    val sender = document.getString("sender") ?: "DefaultSender"
                    val content = document.getString("content") ?: "DefaultContent"
                    val timestamp = document.getLong("timestamp") ?: 0

                    val message = Message(sender, content, timestamp)
                    messagesList.add(message)
                }

                val adapter = MessageAdapter(this, messagesList)
                recyclerview.adapter = adapter
                adapter.notifyDataSetChanged()

            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 처리
                Log.e(TAG, "Error getting messages: $exception")
                Toast.makeText(this, "메시지 가져오기 실패: $exception", Toast.LENGTH_SHORT).show()
            }

        // Remote Config 설정
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config)
    }
}