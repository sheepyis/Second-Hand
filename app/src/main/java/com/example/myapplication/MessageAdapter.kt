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

class MessageAdapter(private val context: Context, private val messagesList: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var nickname: String



    init {
        // 어댑터 초기화 시에 닉네임을 가져옴
        val user = Firebase.auth.currentUser
        val userId = user?.uid ?: ""

        firestore = FirebaseFirestore.getInstance()

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    nickname = document.getString("nickname") ?: ""
                    notifyDataSetChanged() // 어댑터를 업데이트
                }
            }
            .addOnFailureListener { exception ->
                // 오류 처리
            }
    }

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
        // 닉네임이 초기화되지 않았으면 아무 작업도 하지 않음
        if (!::nickname.isInitialized) return

        val currentItem = messagesList[position]
        if (currentItem.receiver == nickname) {
            holder.sender.text = "${currentItem.sender}님:"
            holder.product.text = currentItem.product
            holder.content.text = currentItem.content
        }
        else {
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.height = 0
            params.width = 0
            holder.itemView.layoutParams = params
            holder.itemView.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }
}
