package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddActivity : AppCompatActivity() {

    private lateinit var editTitle: EditText
    private lateinit var editDetail: EditText
    private lateinit var editPrice: EditText

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_layout)

        editTitle = findViewById(R.id.editTextText)
        editDetail = findViewById(R.id.editTextText2)
        editPrice = findViewById(R.id.editTextText3)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val productCollectionRef = db.collection("product")

        val addButton = findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid

                val userRef = db.collection("users").document(userId)

                userRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val nickname = documentSnapshot.getString("nickname")

                        if (nickname != null) {
                            val title = editTitle.text.toString()
                            val detail = editDetail.text.toString()
                            val priceStr = editPrice.text.toString()

                            if (title.isNotEmpty() && detail.isNotEmpty() && priceStr.isNotEmpty()) {
                                val price = priceStr.toInt()

                                val itemMap = hashMapOf(
                                    "title" to title,
                                    "detail" to detail,
                                    "price" to price,
                                    "nickname" to nickname
                                )

                                productCollectionRef.add(itemMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(this,"상품이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, HomeActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this,"상품 등록 실패: 모든 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "입력 필드를 모두 채워주세요.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "사용자의 닉네임을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "로그인 해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}