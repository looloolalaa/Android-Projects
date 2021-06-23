package com.example.db_copy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.db_copy.MyDBHelper
import com.example.db_copy.Product
import com.example.db_copy.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    lateinit var myDBHelper: MyDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDB()
        init()
        getAllRecord()
    }

    private fun initDB() {
        val dbfile=this.getDatabasePath("mydb.db")
        if(!dbfile.parentFile.exists())  //디렉토리 없으면
            dbfile.parentFile.mkdir()   //디렉토리 생성
        if(!dbfile.exists()){
            val file =resources.openRawResource(R.raw.mydb)
            val fileSize=file.available()
            val buffer=ByteArray(fileSize)
            file.read(buffer)
            file.close()

            dbfile.createNewFile()
            val output=FileOutputStream(dbfile)
            output.write(buffer)
            output.close()

        }
    }

    fun init(){

        myDBHelper= MyDBHelper(this)

        insertBtn.setOnClickListener {

            val name=pNameEdit.text
            val quantity=pQuantityEdit.text
            if(name.isEmpty() || quantity.isEmpty()){
                Toast.makeText(this,"isEmpty",Toast.LENGTH_SHORT).show()
                vacate()
            }else {

                val product = Product(0, name.toString(), quantity.toString().toInt())
                val result = myDBHelper.insertProduct(product)   //삽입
                if (result) {
                    Toast.makeText(this, "INSERT SUCCESS", Toast.LENGTH_SHORT).show()
                    getAllRecord()
                } else {
                    Toast.makeText(this, "INSERT FAILED", Toast.LENGTH_SHORT).show()
                }
                vacate()
            }
        }

        deleteBtn.setOnClickListener {
            val id=pIdEdit.text.toString()
            if(id.isEmpty()){
                Toast.makeText(this,"isEmpty",Toast.LENGTH_SHORT).show()
                getAllRecord()
                vacate()
            }else {
                val result = myDBHelper.deleteProduct(id)
                if (result) {
                    Toast.makeText(this, "DELETE SUCCESS", Toast.LENGTH_SHORT).show()
                    getAllRecord()
                } else {
                    Toast.makeText(this, "DELETE FAILED", Toast.LENGTH_SHORT).show()
                }
                vacate()
            }
        }

        updateBtn.setOnClickListener {
            val id=pIdEdit.text.toString()       //id 먼저 확인
            if(id.isEmpty()){
                Toast.makeText(this,"isEmpty",Toast.LENGTH_SHORT).show()
                getAllRecord()
                vacate()
            }else {
                val name=pNameEdit.text.toString()
                val quantity=pQuantityEdit.text.toString().toInt()
                val product=Product(id.toInt(),name,quantity)
                val result = myDBHelper.updateProduct(product)
                if (result) {
                    Toast.makeText(this, "UPDATE SUCCESS", Toast.LENGTH_SHORT).show()
                    getAllRecord()
                } else {
                    Toast.makeText(this, "UPDATE FAILED", Toast.LENGTH_SHORT).show()
                }
                vacate()
            }
        }

        findBtn.setOnClickListener {
            val name=pNameEdit.text.toString()
            if(name.isEmpty()){
                Toast.makeText(this,"isEmpty",Toast.LENGTH_SHORT).show()
                getAllRecord()
                vacate()
            }else {

                val result = myDBHelper.findProduct(name)
                if (result) {
                    Toast.makeText(this, "FIND SUCCESS", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "FIND FAILED", Toast.LENGTH_SHORT).show()
                }
                vacate()
            }
        }

        testSql.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                myDBHelper.findProduct2(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")
            }

        })

    }

    fun getAllRecord(){
        myDBHelper.getAllRecord()
    }

    fun vacate(){
        pIdEdit.setText("")
        pNameEdit.setText("")
        pQuantityEdit.setText("")
    }

}
