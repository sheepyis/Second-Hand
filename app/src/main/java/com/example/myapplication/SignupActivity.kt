package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

//import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val signup = findViewById<Button>(R.id.signup)
        val email = findViewById<TextView>(R.id.email)
        val password = findViewById<TextView>(R.id.password)
        val nickname = findViewById<TextView>(R.id.nickname)

        signup.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()
            val nicknameText = nickname.text.toString().trim()

            if (emailText.isEmpty() || passwordText.isEmpty() || nicknameText.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                createUser(emailText, passwordText, nicknameText)
            }
        }

        val cancelButton = findViewById<Button>(R.id.cancel)
        cancelButton.setOnClickListener {
            // LoginActivity로 이동하기 위한 인텐트 생성
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    // createUser 함수 내부 수정
    private fun createUser(emailText: String, passwordText: String, nickname: String) {
        auth.createUserWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                    // 2초 후에 LoginActivity로 이동
                    val handler = android.os.Handler()
                    handler.postDelayed({
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("NICKNAME", nickname)
                        startActivity(intent)
                        finish()  // SignupActivity를 종료하여 백 스택에 남지 않도록 함
                    }, 2000)
                } else {
                    Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }




}