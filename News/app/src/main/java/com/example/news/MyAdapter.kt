package com.example.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val items:ArrayList<MyData>)
    :RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    var itemClickListener:OnItemClickListener?=null
    interface OnItemClickListener{
        fun OnItemClick(holder:MyViewHolder, view: View, data:MyData, position:Int)
    }

    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var newstitle:TextView=itemView.findViewById(R.id.newsTitle)
        init {
            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(this,it,items[adapterPosition],adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        holder.newstitle.text=items[position].newstitle
    }


}