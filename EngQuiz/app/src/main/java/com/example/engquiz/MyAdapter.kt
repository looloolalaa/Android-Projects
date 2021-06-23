package com.example.engquiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val items: ArrayList<String>, val n:Int): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){


    lateinit var circleHolder :MyViewHolder  //정답 뷰홀더 == 5개중 n번째

    var listener: OnClickListener ?=null
    interface OnClickListener{
        fun onClickFun(holder: MyViewHolder, view: View, data: String, position: Int)
    }

    inner class MyViewHolder(val item: View): RecyclerView.ViewHolder(item){
        var textView:TextView = item.findViewById(R.id.textView)

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
        holder.textView.text = items[position]
        if(position==n)           //포지션이 정답번째에 해당하면
            circleHolder = holder  //정답뷰홀더
    }

}