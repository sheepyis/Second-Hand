package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
data class Product(val title: String, val detail: String, val price: Int)
class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)
class ProductAdapter(private var productList: List<Product>)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    fun interface OnItemClickListener {
        fun onItemClick(product_id: String)
    }
    private var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        itemClickListener = listener
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.productTitle)
        val detailTextView: TextView = view.findViewById(R.id.productPrice)
        val priceTextView: TextView = view.findViewById(R.id.productPrice)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.titleTextView.text = product.title
        holder.detailTextView.text = product.detail
        holder.priceTextView.text = product.price.toString()
    }

    override fun getItemCount() = productList.size
}