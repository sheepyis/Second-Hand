package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.startButton)
        val loginButton = findViewById<TextView>(R.id.loginText)

        startButton.setOnClickListener {
            // SignUpActivity로 이동하기 위한 인텐트 생성
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            // LoginActivity로 이동하기 위한 인텐트 생성
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}
