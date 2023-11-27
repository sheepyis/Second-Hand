package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button

import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ProductUpdateActivity: AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    private val updateButton by lazy { findViewById<Button>(R.id.addButton) }
    private val producttitle by lazy { findViewById<TextView>(R.id.textView1) } //제목
    private val productcontent by lazy { findViewById<TextView>(R.id.textView2) } //내용
    private val productprice by lazy { findViewById<TextView>(R.id.editTextText3) } //가격
    private val switchcontent by lazy { findViewById<Switch>(R.id.switch2) } //스위치


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_product)

        val id = intent.getStringExtra("id")//아이디 값 - 수정하기 할때 사용할꺼임
        var sold = intent.getStringExtra("sold")

        val updateButton = findViewById<Button>(R.id.updateBack)

        updateButton.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        producttitle.text = intent.getStringExtra("title") //제목
        productprice.text = intent.getStringExtra("price").toString() //가격
        productcontent.text = intent.getStringExtra("detail") //디테일

        if (sold == "true") {
            switchcontent.text = "판매중"
            switchcontent.isChecked = true
        } else {
            switchcontent.text = "판매완료"
            switchcontent.isChecked = false
        }

        switchcontent.setOnCheckedChangeListener { _, isChecked ->
            sold = if (isChecked) {
                "true"
            } else {
                "false"
            }

            switchcontent.text = if (isChecked) {
                "판매중"
            } else {
                "판매완료"
            }
        }

        updateButton.setOnClickListener {
            val newPriceStr = productprice.text.toString()

            if (newPriceStr.isNotEmpty()) {
                val newPrice = newPriceStr.toInt()
                val newSale = switchcontent.isChecked


                val itemMap: Map<String, Any> = mapOf(
                    "price" to newPrice,
                    "sale" to newSale
                )

                db.collection("product").document(id.toString())
                    .update(itemMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "수정 완료에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}