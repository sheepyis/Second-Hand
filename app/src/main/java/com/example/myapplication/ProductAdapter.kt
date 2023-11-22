package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

data class Product(val title: String, val nickname: String, val price: Int, val sold: String, val detail : String, val id : String){
    constructor(doc: QueryDocumentSnapshot) :
            this(doc["title"].toString(),doc["nickname"].toString(), doc["price"].toString().toIntOrNull() ?: 0,
                doc["sale"].toString(), doc["detail"].toString(), doc.id
            )
    //this(doc.id, doc["title"].toString(), doc["price"].toString().toIntOrNull() ?: 0)
    //constructor(key: String, map: Map<*, *>) :
            //this(key, map["title"].toString(), map["price"].toString().toIntOrNull() ?: 0)
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

    private lateinit var firestore: FirebaseFirestore
    private var nickname :String = ""
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productList[position]
        holder.view.findViewById<TextView>(R.id.productTitle).text = product.title
        holder.view.findViewById<TextView>(R.id.productSeller).text = product.nickname
        holder.view.findViewById<TextView>(R.id.productPrice).text = product.price.toString()
        if(product.sold.equals("true"))
        {
            holder.view.findViewById<ImageView>(R.id.imageView4).setImageResource(R.drawable.proc)
            holder.view.findViewById<TextView>(R.id.productsoldout).text = "판매중"

        }else{
            holder.view.findViewById<ImageView>(R.id.imageView4).setImageResource(R.drawable.backgroundimagesold)
            holder.view.findViewById<TextView>(R.id.productsoldout).text = "판매완료"

        }
        val user = Firebase.auth.currentUser
        val userId = user?.uid ?: ""
        firestore = FirebaseFirestore.getInstance()

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener{ document ->
                if(document !=null){
                    nickname = document.getString("nickname")?:""
                }

            }

        holder.view.setOnClickListener {
            if(nickname != product.nickname){
                val DetailIntent = Intent(context, ProductDetailActivity::class.java)
                DetailIntent.putExtra("id",product.id)
                DetailIntent.putExtra("seller", product.nickname)
                DetailIntent.putExtra("title",product.title)
                DetailIntent.putExtra("price",product.price.toString())
                DetailIntent.putExtra("sold",product.sold)
                DetailIntent.putExtra("detail",product.detail)
                context.startActivity(DetailIntent)
            }else{
                val UpdateIntent = Intent(context,ProductUpdateActivity::class.java )
                UpdateIntent.putExtra("id",product.id)
                UpdateIntent.putExtra("title",product.title)
                UpdateIntent.putExtra("price",product.price.toString())
                UpdateIntent.putExtra("sold",product.sold)
                UpdateIntent.putExtra("detail",product.detail)
                context.startActivity(UpdateIntent)
            }
        }
    }

    override fun getItemCount() = productList.size
}
