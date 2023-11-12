package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var nicknameTextView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        nicknameTextView = findViewById(R.id.textView)

        val currentUserEmail = Firebase.auth.currentUser?.email
        nicknameTextView.text = "${currentUserEmail}님"
    }
}
