package com.example.myapplication

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.graphics.BitmapFactory
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.ktx.storage
data class Product(val title: String, val detail: String, val price: Int)
class HomeActivity : AppCompatActivity() {
    private lateinit var nicknameTextView: TextView
    private lateinit var firestore: FirebaseFirestore

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productList: MutableList<Product>
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        firestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.recyclerview)
        productList = mutableListOf()

        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(productList)
        productAdapter?.setOnItemClickListener {
            queryItem(it)
        }



        // 홈 화면에서 닉네임 표시
        val user = Firebase.auth.currentUser
        val userId = user?.uid ?: ""

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val nickname = document.getString("nickname") ?: ""
                    nicknameTextView = findViewById(R.id.textView)
                    nicknameTextView.text = "${nickname}님"
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "닉네임 불러오기 실패: $exception", Toast.LENGTH_SHORT).show()
            }

        displayImage()

        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config)

        val textView12 = findViewById<TextView>(R.id.textView12)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) {
                val SecondHands = remoteConfig.getBoolean("SecondHands")
                textView12.text = "{$SecondHands}"
            }



    // 페이지 이동 버튼들
        val imageButton2 = findViewById<ImageButton>(R.id.imageButton2)
        imageButton2.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        val imageButton3 = findViewById<ImageButton>(R.id.imageButton3)
        imageButton3.setOnClickListener{
            val chatIntent = Intent(this, ChatActivity::class.java)
            startActivity(chatIntent)
        }

    }
    private fun queryItem(itemID: String) {
        db = FirebaseFirestore.getInstance()
        val productCollectionRef = db.collection("product")
        productCollectionRef.document(itemID).get()
            .addOnSuccessListener {
                editID.setText(it.id)
                checkAutoID.isChecked = false
                editID.isEnabled = true
                editItemName.setText(it["name"].toString())
                editPrice.setText(it["price"].toString())
            }.addOnFailureListener {
            }
    }
    fun displayImage() {
        val storageRef = Firebase.storage.reference
        val imageRef = Firebase.storage.getReferenceFromUrl(
            "gs://second-hands-9f426.appspot.com/android.png"
        )

        val view = findViewById<ImageView>(R.id.imageView5)
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            // 이미지 다운로드 실패
        }
    }
}