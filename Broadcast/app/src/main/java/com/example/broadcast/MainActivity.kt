package com.example.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    lateinit var mybr:BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPermission()
        init()
        if(intent.hasExtra("msg")){
            val msg=intent.getStringExtra("msg")
            Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(intent!=null){
            if(intent.hasExtra("msg")){
                val msg=intent.getStringExtra("msg")
                Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun init(){
        mybr=object :BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent!=null){
                    if(intent.action.equals(Intent.ACTION_POWER_CONNECTED)){
                        Toast.makeText(context,"배터리 충전 시작",Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        val filter=IntentFilter()
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        registerReceiver(mybr,filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mybr)
    }

    fun initPermission(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"문자 수신 동의하셨습니다.",Toast.LENGTH_SHORT).show()
        }else{              //동의 요청
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.RECEIVE_SMS),100)
        }

    }

    override fun onRequestPermissionsResult(      //권한 요청 승인,거부에 따른 결과 작업
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==100){  //사용자가 승인 시
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"문자 수신 동의하셨습니다.",Toast.LENGTH_SHORT).show()
            }else{   //사용자가 거부 시
                Toast.makeText(this, "문자 수신 동의가 필요합니다.",Toast.LENGTH_SHORT).show()
            }
        }

    }



}
