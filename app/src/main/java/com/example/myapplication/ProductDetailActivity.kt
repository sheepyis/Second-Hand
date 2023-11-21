package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ProductDetailActivity : AppCompatActivity(){
    private val productsold by lazy {findViewById<TextView>(R.id.sale)} //판매함?
    private val titleTextView by lazy { findViewById<TextView>(R.id.productsoldout)} //물건제목
    private val priceTextView by lazy {findViewById<TextView>(R.id.costText)} //물건가격
    private val detailTextView by lazy {findViewById<TextView>(R.id.textView)} //물건설명
    private val sellerTextView by lazy {findViewById<TextView>(R.id.produceName)} //물건 판매자
    private val chatButton by lazy {findViewById<Button>(R.id.chatButton)} //채팅하기 버튼
    private val textSnapshotListener by lazy { findViewById<TextView>(R.id.textSnapshotListener) }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail)

        val title = intent.getStringExtra("title")
        val seller = intent.getStringExtra("seller")
        val price = intent.getStringExtra("price")
        val sold = intent.getStringExtra("sold")
        val detail = intent.getStringExtra("detail")

        titleTextView.text = title
        priceTextView.text = price.toString()
        detailTextView.text = detail
        sellerTextView.text = seller
        if(sold == "true"){
            productsold.text="판매중"
        }else{
            productsold.text = "판매완료"
        }
        chatButton.setOnClickListener {
            val intent = Intent(this@ProductDetailActivity, ChatActivity::class.java)
            intent.putExtra("sellerNickname", seller)
            startActivity(intent)
        }



    }

}