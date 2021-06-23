package com.example.firebasedb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import java.text.FieldPosition

class ProductAdapter(options: FirebaseRecyclerOptions<Product>) :
    FirebaseRecyclerAdapter<Product, ProductAdapter.ViewHolder>(options) {

    var itemClickListener:OnItemClickListener?=null
    interface OnItemClickListener{
        fun OnItemClick(view:View,position:Int)
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var productId:TextView
        var productName:TextView
        var productQuantity:TextView
        init {
            productId=itemView.findViewById(R.id.productId)
            productName=itemView.findViewById(R.id.productName)
            productQuantity=itemView.findViewById(R.id.productQuantity)

            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(it,adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.row,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Product) {
        holder.productId.text=model.pid.toString()
        holder.productName.text=model.pName
        holder.productQuantity.text=model.pQuantity.toString()
    }


}