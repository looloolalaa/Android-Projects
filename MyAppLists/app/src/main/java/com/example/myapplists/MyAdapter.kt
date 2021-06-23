package com.example.engquiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplists.R

class MyAdapter(val items: ArrayList<MyData>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){


    lateinit var circleHolder :MyViewHolder  //정답 뷰홀더 == 5개중 n번째

    var listener: OnClickListener ?=null
    interface OnClickListener{
        fun onClickFun(holder: MyViewHolder, view: View, data: MyData, position: Int)
    }

    inner class MyViewHolder(val item: View): RecyclerView.ViewHolder(item){
        var imageView:ImageView=item.findViewById(R.id.imageView)
        var textView:TextView = item.findViewById(R.id.textView)
        var textView2:TextView = item.findViewById(R.id.textView2)

        init{
            item.setOnClickListener {
                //
                listener?.onClickFun(this, it, items[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.imageView.setImageDrawable(items[position].appIcon)
        holder.textView.text = items[position].appLabel
        holder.textView2.text = items[position].appClass

    }

}