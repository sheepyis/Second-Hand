package com.example.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.startButton)


        startButton.setOnClickListener {
            // SignUpActivity로 이동하기 위한 인텐트 생성
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


//        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//            val textFCMToken = findViewById<TextView>(R.id.textView2)
//            textFCMToken.text = if (task.isSuccessful) task.result else "Token Error!"
//
//            // 클립보드에 복사
//            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val clip = ClipData.newPlainText("FCM Token", textFCMToken.text)
//            clipboard.setPrimaryClip(clip)
//
//            // 로그에 출력
//            Log.d(ChatActivity.TAG, "FCM token: ${textFCMToken.text}")
//        }
    }

}
