package com.example.myapplication

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(private val context: Context, private val messagesList: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private lateinit var firestore: FirebaseFirestore
    lateinit var nickname: String

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sender: TextView = itemView.findViewById(R.id.sender)
        val product: TextView = itemView.findViewById(R.id.product)
        val content: TextView = itemView.findViewById(R.id.content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val user = Firebase.auth.currentUser
        val userId = user?.uid ?: ""

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    nickname = document.getString("nickname") ?: ""

                }
            }
            .addOnFailureListener { exception ->
                //Toast.makeText(this, "닉네임 불러오기 실패: $exception", Toast.LENGTH_SHORT).show()
            }
        val currentItem = messagesList[position]
        if(currentItem.receiver==nickname) {
            holder.sender.text = "${currentItem.sender}님:"
            holder.product.text = currentItem.product
            holder.content.text = currentItem.content
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }
}
