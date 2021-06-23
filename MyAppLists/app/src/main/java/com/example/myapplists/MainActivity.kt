package com.example.myapplists

import android.content.Intent
import android.content.Intent.ACTION_MAIN
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.engquiz.MyAdapter
import com.example.engquiz.MyData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    fun init(){
        recyclerView.layoutManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        adapter= MyAdapter(ArrayList<MyData>())
        recyclerView.adapter=adapter

        val i=Intent(ACTION_MAIN)           //메인과
        i.addCategory(Intent.CATEGORY_LAUNCHER)   //런처카테고리를 갖는 인텐트 생성
        val appList=packageManager.queryIntentActivities(i,0)
        //해당 조건을 만족하는 액티비티가 있나 찾아서 리스트로 반환

        if(appList.size>0){
            for(appInfo in appList){
                val appLabel=appInfo.loadLabel(packageManager)
                val appClass=appInfo.activityInfo.name
                val appPack=appInfo.activityInfo.packageName
                val appIcon=appInfo.loadIcon(packageManager)
                adapter.items.add(MyData(appLabel.toString(),appClass,appPack,appIcon))
            }
        }

        adapter.listener=object : MyAdapter.OnClickListener{
            override fun onClickFun(
                holder: MyAdapter.MyViewHolder,
                view: View,
                data: MyData,
                position: Int
            ) {
                val i=packageManager.getLaunchIntentForPackage(data.appPack)
                startActivity(i)
            }

        }

    }
}
