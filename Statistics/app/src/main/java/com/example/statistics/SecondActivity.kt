package com.example.statistics

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.core.view.size
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_second.*
import org.json.JSONArray
import java.util.stream.IntStream.range

class SecondActivity : AppCompatActivity() {

    lateinit var json:JSONArray
    var attributeName:MutableList<String> = mutableListOf()
    var row:MutableList<String> = mutableListOf()
    var itemNum=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val i= intent
        val jsonstr=i.getStringExtra("json")
        json=JSONArray(jsonstr)

        init()
        draw()
    }

    fun draw(){

        tableLayout.removeAllViewsInLayout()

        val tableRow= TableRow(this)
        val rowParam=TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
            10,attributeName.size.toFloat())
        tableRow.layoutParams=rowParam

        val viewParam=TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT,1f)

        val kind=attributeName.size-itemNum  //2

        for(i in 0 until attributeName.size){           //제목 row
            val textView= Button(this)
            textView.layoutParams=viewParam
            if(i>=kind)     //제목
                textView.text=attributeName[i]
            else
                textView.text=attributeName[i]
            textView.setBackgroundColor(resources.getColor(R.color.table))
            textView.textSize=15.0f
            textView.gravity= Gravity.CENTER

            if(i>=kind){   //data row

                textView.setOnClickListener{

                    var keys= arrayListOf<String>()
                    var values= arrayListOf<String>()

                    for(j in 2 until tableLayout.childCount){
                        val tableRow=tableLayout[j]
                        val row=tableRow as TableRow

                        var keyStr=""
                        for(k in 0 until kind){
                            keyStr+=(row[k] as TextView).text.toString() +" "
                        }
                        keys.add(keyStr)

                        var dt=(row[i] as TextView).text.toString()
                        if(dt=="-"||dt=="*"){
                           dt="0"
                        }
                        values.add(dt)


                    }
                    val i=Intent(this,ThirdActivity::class.java)
                    i.putExtra("title",textView.text.toString())
                    i.putExtra("keys",keys)
                    i.putExtra("values",values)
                    startActivity(i)


                }
            }
            tableRow.addView(textView)
        }
        tableLayout.addView(tableRow)

        val tableRow2= TableRow(this)
        val rowParam2=TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT,attributeName.size.toFloat())
        tableRow2.layoutParams=rowParam2

        val viewParam2=TableRow.LayoutParams(0,40,1f)

        for(i in 0 until attributeName.size*3){           //제목 버튼 row

            val textView= TextView(this)
            textView.layoutParams=viewParam2
            if(i%3==0)
                textView.text="▲"   //오름차순
            else if(i%3==1)
                textView.text="▼"   //내림차순
            else if(i%3==2)
                textView.text="■"
            textView.setBackgroundColor(resources.getColor(R.color.button))
            textView.textSize=8.0f
            textView.gravity= Gravity.CENTER

            if(i>=kind*3){   //data row

                textView.setOnClickListener{
                    if(i%3==0){
                        rowSort_up(i/3)   //오름
                    }else if(i%3==1){
                        rowSort_down(i/3)   //내림림
                   }else{
                        draw()
                    }
                }
            }
            tableRow2.addView(textView)
        }
        tableLayout.addView(tableRow2)



        for(i in 0 until json.length()){             //데이터 row

            val jObject=json.getJSONObject(i)
            if(i%itemNum==0) {    //열의 첫번째면
                row.clear()
                for (j in 0 until kind) { // 분류 추가
                    val keyStr = "C" + (j + 1) + "_NM"
                    row.add(jObject[keyStr].toString())
                }
            }
            row.add(jObject["DT"].toString())   //데이터 추가

            if(row.size==attributeName.size){    //row
                val tableRow= TableRow(this)
                val rowParam=TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    10,attributeName.size.toFloat())
                tableRow.layoutParams=rowParam
                val viewParam=TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT,1f)

                for(j in 0 until row.size){           //제목 row
                    val textView= TextView(this)
                    textView.layoutParams=viewParam
                    textView.text=row[j]  //데이터
                    if(((i+1)/itemNum)%2==0)
                        textView.setBackgroundColor(resources.getColor(R.color.lightGRAY))
                    textView.textSize=15.0f
                    textView.gravity= Gravity.CENTER
                    tableRow.addView(textView)
                }
                tableLayout.addView(tableRow)
            }

        }

    }

    fun init(){


        val firstObject = json.getJSONObject(0)
        val title=firstObject["TBL_NM"].toString()
        titleView.setText(title)

        var kind=0   //분류 개수
        for(key in firstObject.keys()){
            if(key.contains("_OBJ_NM") && key.length<10)
                kind++
        }

        for(i in 0 until kind){ // 분류 추가
            val keyStr="C"+(i+1)+"_OBJ_NM"
            attributeName.add(firstObject[keyStr].toString())
        }

        val firstItemName=firstObject["ITM_NM"].toString()
        attributeName.add(firstItemName)
        for(i in 1 until json.length()){   //Item 추가
            val jObject=json.getJSONObject(i)
            val itemName=jObject["ITM_NM"].toString()
            if(firstItemName==jObject["ITM_NM"])
                break
            attributeName.add(itemName)
        }
        itemNum=attributeName.size-kind


    }

    fun rowSort_up(x:Int){  //오름차순           //6
        for(start in 1 until tableLayout.childCount) {
            val end=tableLayout.childCount-start
            for (i in 2 until end) {
                val first = tableLayout[i] as TableRow
                val second = tableLayout[i + 1] as TableRow
                val firstDataStr = (first[x] as TextView).text.toString()
                val secondDataStr = (second[x] as TextView).text.toString()
                val firstData=when(firstDataStr){
                    "*","-"->0F
                    else->firstDataStr.toFloat()
                }
                val secondData=when(secondDataStr){
                    "*","-"->0F
                    else->secondDataStr.toFloat()
                }

                if (firstData > secondData) {
                    rowChange(i, i + 1)
                }
            }
        }
    }

    fun rowSort_down(x:Int){  //내림차순
        for(start in 1 until tableLayout.childCount) {
            val end=tableLayout.childCount-start
            for (i in 2 until end) {
                val first = tableLayout[i] as TableRow
                val second = tableLayout[i + 1] as TableRow
                val firstDataStr = (first[x] as TextView).text.toString()
                val secondDataStr = (second[x] as TextView).text.toString()
                val firstData=when(firstDataStr){
                    "*","-"->0F
                    else->firstDataStr.toFloat()
                }
                val secondData=when(secondDataStr){
                    "*","-"->0F
                    else->secondDataStr.toFloat()
                }

                if (firstData < secondData) {
                    rowChange(i, i + 1)
                }
            }
        }
    }

    fun rowChange(a:Int,b:Int){
        val first=tableLayout[a] as TableRow
        val last=tableLayout[b] as TableRow
        for(i in 0 until attributeName.size){
            val textView1=first[i] as TextView
            val textView2=last[i] as TextView
            val temp=textView1.text
            textView1.text=textView2.text
            textView2.text=temp
        }
    }

}
