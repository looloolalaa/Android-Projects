package com.example.firebasedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: ProductAdapter
    lateinit var rdb:DatabaseReference

    var findQuery=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        initBtn()
    }

    fun initBtn(){

        insertBtn.setOnClickListener {
            initAdapter()

            val item=Product(
                Integer.parseInt(pIdEdit.text.toString()),
                pNameEdit.text.toString(),
                Integer.parseInt(pQuantityEdit.text.toString())
            )
            rdb.child(pIdEdit.text.toString()).setValue(item)         //id로 찾고 setValue -> insert
            vacate()
        }

        deleteBtn.setOnClickListener {
            initAdapter()
            rdb.child(pIdEdit.text.toString()).removeValue()    //id로 찾고 객체삭제
            vacate()
        }

        updateBtn.setOnClickListener {
            initAdapter()
            rdb.child(pIdEdit.text.toString())              //id로 찾고 값 변경
                .child("pquantity")
                .setValue(Integer.parseInt(pQuantityEdit.text.toString()))
            vacate()
        }

        findBtn.setOnClickListener {
            if(findQuery)
                findQueryAdapter()
            else {
                findQuery=true
                findQueryAdapter()
            }
        }

    }

    fun initAdapter(){       //전체 데이터가져오는 쿼리로 재초기화하는 함수
        if(findQuery){
            findQuery=false
            if(adapter!=null)
                adapter.stopListening()
            val query=FirebaseDatabase.getInstance().reference.child("Products/items").limitToLast(50)
            val option=FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query,Product::class.java)
                .build()
            adapter= ProductAdapter(option)
            recyclerView.adapter=adapter
            adapter.startListening()
        }
    }

    fun findQueryAdapter(){        //find쿼리로 어답터 재설정하는 함수
        if(adapter!=null)
            adapter.stopListening()
        val query=rdb.orderByChild("pname").equalTo(pNameEdit.text.toString())
        val option=FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(query,Product::class.java)
            .build()
        adapter= ProductAdapter(option)

        adapter.itemClickListener=object :ProductAdapter.OnItemClickListener{

            override fun OnItemClick(view: View, position: Int) {         //adapter.getItem(position) == model:Product
                pIdEdit.setText(adapter.getItem(position).pid.toString())
                pNameEdit.setText(adapter.getItem(position).pName)
                pQuantityEdit.setText(adapter.getItem(position).pQuantity.toString())

            }

        }
        recyclerView.adapter=adapter
        adapter.startListening()
    }

    fun init(){
        layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager=layoutManager

        rdb=FirebaseDatabase.getInstance().getReference("Products/items")
        val query=FirebaseDatabase.getInstance().reference.child("Products/items").limitToLast(50)
        //최근 삽입된 50개 데이터를 가져오라는 질의

        val option=FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(query,Product::class.java)
            .build()
        //질의에 해당하는 결과

        adapter= ProductAdapter(option)
        adapter.itemClickListener=object :ProductAdapter.OnItemClickListener{

            override fun OnItemClick(view: View, position: Int) {         //adapter.getItem(position) == model:Product
                pIdEdit.setText(adapter.getItem(position).pid.toString())
                pNameEdit.setText(adapter.getItem(position).pName)
                pQuantityEdit.setText(adapter.getItem(position).pQuantity.toString())

            }

        }
        recyclerView.adapter=adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    fun vacate(){
        pIdEdit.setText("")
        pNameEdit.setText("")
        pQuantityEdit.setText("")
    }
}
