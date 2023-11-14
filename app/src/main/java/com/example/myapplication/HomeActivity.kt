package com.example.myapplication


import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.ktx.storage

class HomeActivity : AppCompatActivity() {
    private lateinit var nicknameTextView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        nicknameTextView = findViewById(R.id.textView)

        val currentUserEmail = Firebase.auth.currentUser?.email
        nicknameTextView.text = "${currentUserEmail}님"
        displayImage()

        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1 // For test purpose only, 3600 seconds for production
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config)

        val textView12 = findViewById<TextView>(R.id.textView12)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { // it: task
                val SecondHands = remoteConfig.getBoolean("SecondHands")
                textView12.text="{$SecondHands}"
            }

    }

    fun displayImage(){
        val storageRef = Firebase.storage.reference // reference to root
        // val imageRef1 = storageRef.child("images/computer_sangsangbugi.jpg")
        val imageRef = Firebase.storage.getReferenceFromUrl(
            "gs://second-hands-9f426.appspot.com/android.png"
        )

        val view = findViewById<ImageView>(R.id.imageView5)
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            // Failed to download the image
        }
    }
}
