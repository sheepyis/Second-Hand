package com.example.myapplication

import android.os.Bundle
import android.widget.Button

import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProductUpdateActivity: AppCompatActivity(){

    private val updateButton by lazy {findViewById<Button>(R.id.addButton)}
    private val producttitle by lazy {findViewById<TextView>(R.id.textView1)} //제목
    private val productcontent by lazy { findViewById<TextView>(R.id.textView2)} //내용
    private val productprice by lazy {findViewById<TextView>(R.id.editTextText3)} //가격
    private val switchcontent by lazy {findViewById<Switch>(R.id.switch2)} //스위치


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_product)

        val id =intent.getStringExtra("id") //아이디 값 - 수정하기 할때 사용할꺼임
        var sold = intent.getStringExtra("sold")

        producttitle.text = intent.getStringExtra("title") //제목
        productprice.text = intent.getStringExtra("price").toString() //가격
        productcontent.text = intent.getStringExtra("detail") //디테일

        if(sold == "true"){
            switchcontent.text = "판매중"
            switchcontent.isChecked = true
        }else{
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


    }
}