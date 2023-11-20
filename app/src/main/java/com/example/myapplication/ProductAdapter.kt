package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.QueryDocumentSnapshot

data class Product(val title: String, val nickname: String, val price: Int){
    constructor(doc: QueryDocumentSnapshot) :
            this(doc["title"].toString(),doc["nickname"].toString(), doc["price"].toString().toIntOrNull() ?: 0)
    //this(doc.id, doc["title"].toString(), doc["price"].toString().toIntOrNull() ?: 0)
    constructor(key: String, map: Map<*, *>) :
            this(key, map["title"].toString(), map["price"].toString().toIntOrNull() ?: 0)
}
class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

class ProductAdapter(private val context: Context, private var productList: List<Product>) : RecyclerView.Adapter<MyViewHolder>() {
    // 인터페이스 선언을 외부로 빼내어 사용할 수 있도록 수정
    interface OnItemClickListener {
        fun onItemClick(product_id: String)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    fun updateList(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder  {
        val inflater = LayoutInflater.from(parent.context)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productList[position]
        holder.view.findViewById<TextView>(R.id.productTitle).text = product.title
        holder.view.findViewById<TextView>(R.id.productSeller).text = product.nickname
        holder.view.findViewById<TextView>(R.id.productPrice).text = product.price.toString()
    }



    override fun getItemCount() = productList.size
}
