package com.example.engvoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.zip.Inflater

class MyAdapter(val items:ArrayList<String>,val row:Boolean): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    // 어답터는 데이터와 리사이클러를 바인딩하는 역할
    // 리사이클러의 어답터를 상속<뷰홀더>

    var listener:onItemClickListener?=null
    interface onItemClickListener{
        fun itemClick(holder: MyViewHolder, view:View, data:String, position: Int)
    }

    var listener2:onCheckBoxListener?=null
    interface onCheckBoxListener{
        fun ischecked(holder: MyViewHolder, data:String, position: Int)
        fun isNotchecked(holder: MyViewHolder, data:String, position: Int)
    }



    inner class MyViewHolder(item:View):RecyclerView.ViewHolder(item){ // 2
        // 어답터의 최소단위: ViewHolder, 리사이클러의 뷰홀더를 상속하고
        // ViewHolder(v: View)

        var textView: TextView = item.findViewById(R.id.textView)
        var meanView: TextView = item.findViewById(R.id.meaning)
        var checkBox: CheckBox=item.findViewById(R.id.checkBox)
        // 기본단위가 가지는 내부 정보

        init{          //온클릭 이벤트 처리, 미구현
            item.setOnClickListener{
                listener?.itemClick(this, it, items[adapterPosition], adapterPosition)
                //뭔지 모르겠지만 호출하겠다.
            }

            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                when(isChecked){
                    true-> listener2?.ischecked(this, items[adapterPosition], adapterPosition)
                    false->listener2?.isNotchecked(this, items[adapterPosition], adapterPosition)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder { // 1
        // 기본단위인 뷰홀더를 레이아웃에서 추출
        var v:View
        if(row==false)
            v = LayoutInflater.from(parent.context).inflate(R.layout.row, parent,false)
        else
            v = LayoutInflater.from(parent.context).inflate(R.layout.row2, parent,false)

        // 레이아웃 인플레이터로 뷰 생성

        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) { // 3
        // 뷰홀더 작업!!
        holder.textView.text = items[position]
    }

    fun moveItem(oldPos:Int, newPos:Int){
        val item= items.get(oldPos)
        items.removeAt(oldPos)
        items.add(newPos, item)
        notifyItemMoved(oldPos,newPos)
    }

    fun removeItem(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }
}
