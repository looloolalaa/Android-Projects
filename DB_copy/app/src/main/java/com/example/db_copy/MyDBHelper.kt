package com.example.db_copy

import android.content.ContentValues
import android.content.Context
import android.database.AbstractCursor
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MyDBHelper(val context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        val DB_VERSION=1
        val DB_NAME="mydb.db"
        val TABLE_NAME="products"

        val PID="pid"
        val PNAME="pname"
        val PQUANTITY="pquantity"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table="create table if not exists "+TABLE_NAME+         //SQL문
                "("+PID+" integer primary key autoincrement, "+
                PNAME+" text,"+
                PQUANTITY+" integer)"
        db?.execSQL(create_table)  //SQL문 실행
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table="drop table if exists "+TABLE_NAME
        db?.execSQL(drop_table)
        onCreate(db)
    }

    fun insertProduct(product: Product):Boolean{     //데이터 삽입
        val values=ContentValues()
        values.put(PNAME,product.pName)
        values.put(PQUANTITY,product.pQuantity)

        val db=this.writableDatabase  //열고
        val result=db.insert(TABLE_NAME,null,values)  //삽입
        db.close() //닫고
        
        return result>0
    }

    fun deleteProduct(pid:String):Boolean{
        val strsql="select * from "+TABLE_NAME+
                " where "+PID+" = \'"+pid+"\'"
        val db=this.writableDatabase
        val cursor=db.rawQuery(strsql,null)
        if(cursor.count!=0){
            db.delete(TABLE_NAME, PID+"=?", arrayOf(pid))
            cursor.close()
            db.close()
            return true
        }
        cursor.close()
        db.close()
        return false
    }



    fun findProduct(pname:String):Boolean{   //select * from products where pname='새우깡'
        val strsql="select * from "+TABLE_NAME+
                " where "+PNAME+" = \'"+pname+"\'"
        val db=this.readableDatabase
        val cursor=db.rawQuery(strsql,null)
        if(cursor.count!=0){
            showRecord(cursor)
            cursor.close()
            db.close()
            return true
        }
        cursor.close()
        db.close()
        return false

    }

    fun updateProduct(product: Product):Boolean{  //id로 row 찾아서 name,quantity 업뎃
        val strsql="select * from "+TABLE_NAME+
                " where "+PID+" = \'"+product.pid+"\'"
        val db=this.writableDatabase
        val cursor=db.rawQuery(strsql,null)
        if(cursor.moveToFirst()){
            val values=ContentValues()
            values.put(PNAME,product.pName)
            values.put(PQUANTITY,product.pQuantity)
            db.update(TABLE_NAME,values,PID+"=?", arrayOf(product.pid.toString()))
            cursor.close()
            db.close()
            return true
        }
        cursor.close()
        db.close()
        return false
    }

    fun findProduct2(pname:String):Boolean{  //select * from products where pname like '새%'
        val strsql="select * from "+TABLE_NAME+
                " where "+PNAME+" like \'%"+pname+"%\'"
        val db=this.readableDatabase
        val cursor=db.rawQuery(strsql,null)
        if(cursor.count!=0){
            showRecord(cursor)
            cursor.close()
            db.close()
            return true
        }
        cursor.close()
        db.close()
        return false
    }



    fun getAllRecord(){      //모든 Product 출력
        val strsql="select * from "+TABLE_NAME
        val db=this.readableDatabase
        val cursor=db.rawQuery(strsql,null)       //cursor == 질의 결과 테이블
        if(cursor.count!=0){
            showRecord(cursor)
        }
        cursor.close()
        db.close()
    }

    fun showRecord(cursor: Cursor){
        cursor.moveToFirst()

        val activity=context as MainActivity
        activity.tableLayout.removeAllViewsInLayout()

        val tableRow=TableRow(activity)
        val rowParam=TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT,cursor.columnCount.toFloat())
        tableRow.layoutParams=rowParam

        val viewParam=TableRow.LayoutParams(0,100,1f)


        for(i in 0 until cursor.columnCount){           //제목 row
            val textView=TextView(activity)
            textView.layoutParams=viewParam
            textView.text=cursor.getColumnName(i)//제목
            textView.setBackgroundColor(Color.LTGRAY)
            textView.textSize=15.0f
            textView.gravity=Gravity.CENTER
            tableRow.addView(textView)
        }
        activity.tableLayout.addView(tableRow)

        do {
            val row=TableRow(activity)
            row.layoutParams=rowParam
            row.setOnClickListener {
                for(i in 0 until cursor.columnCount){             //내용 row
                    val txtView=row.getChildAt(i) as TextView
                    when(i){
                        0->activity.pIdEdit.setText(txtView.text)
                        1->activity.pNameEdit.setText(txtView.text)
                        2->activity.pQuantityEdit.setText(txtView.text)
                    }
                }
            }

            for(i in 0 until cursor.columnCount){             //내용 row
                val textView=TextView(activity)
                textView.layoutParams=viewParam
                textView.text=cursor.getString(i)//내용
                textView.textSize=12.0f
                textView.gravity=Gravity.CENTER
                textView.setTag(i)
                row.addView(textView)
            }
            activity.tableLayout.addView(row)

        }while (cursor.moveToNext())

    }

}