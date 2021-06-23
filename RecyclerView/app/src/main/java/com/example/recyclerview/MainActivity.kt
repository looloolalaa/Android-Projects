package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var data:ArrayList<MyData> = ArrayList<MyData>()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { //메뉴 생성
        menuInflater.inflate(R.menu.menu1,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {  //메뉴 아이템 선택 시
        recyclerView.layoutManager = when(item.itemId){
            R.id.menuitem1 -> LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            R.id.menuitem2 -> GridLayoutManager(this, 3)
            R.id.menuitem3 -> StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
            else -> null
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager=LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter=MyAdapter(data)
    }

    private fun initData() {
        data.add(MyData("item1", 10))
        data.add(MyData("item2", 20))
        data.add(MyData("item3", 15))
        data.add(MyData("item4", 30))
        data.add(MyData("item5", 25))
        data.add(MyData("item6", 29))
        data.add(MyData("item7", 20))
        data.add(MyData("item8", 8))
        data.add(MyData("item9", 17))
        data.add(MyData("item10", 10))
    }
}
