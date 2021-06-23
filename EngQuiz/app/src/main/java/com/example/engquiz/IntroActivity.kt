package com.example.engquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    val ADDVOC_REQUEST = 100  //추가 액티비티 리퀘스트 상수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        init()
    }

    
    private fun init() {
        vocBtn.setOnClickListener {
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }
        addBtn.setOnClickListener {
            val i = Intent(this, AddVocActivity::class.java)
            startActivityForResult(i,ADDVOC_REQUEST)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode)
        {
            ADDVOC_REQUEST-> {  //들어온 리퀘스트가 추가리퀘스트이고
                if (resultCode == Activity.RESULT_OK) { // RESULT_OK 이면
                    val str = data?.getSerializableExtra("voc") as MyData //인텐트에서 getSerial 로 데이터 뽑기
                    Toast.makeText(this, str.word + " 단어가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
