package com.example.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.zip.Inflater

class MyAdapter(val items:ArrayList<MyData>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    // 어답터는 데이터와 리사이클러를 바인딩하는 역할
    // 리사이클러의 어답터를 상속<뷰홀더>

    inner class MyViewHolder(item:View):RecyclerView.ViewHolder(item){
        // 어답터의 최소단위: ViewHolder, 리사이클러의 뷰홀더를 상속하고
        // ViewHolder(v: View)

        var textView: TextView = item.findViewById(R.id.textView)
        // 기본단위가 가지는 내부 정보
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // 가장 먼저 실행되미
        // 기본단위인 뷰홀더를 레이아웃에서 추출

        val v = LayoutInflater.from(parent.context).inflate(R.layout.row, parent,false)
        // 레이아웃 인플레이터로 뷰 생성

        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // 뷰홀더 작업!!
        holder.textView.text = items[position].name
        holder.textView.textSize = items[position].pt.toFloat()
    }
}