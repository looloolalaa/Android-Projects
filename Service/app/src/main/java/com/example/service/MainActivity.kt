package com.example.service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var num = 0
    var thread:Thread?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    fun init(){
        btnThread.setOnClickListener {
            if(thread==null){
                thread=object :Thread("MyThread"){
                    override fun run() {
                        try {
                            for (i in 0..10){
                                Log.i("MyThread+$num","count : $i")
                                Thread.sleep(1000)
                            }
                        }catch (e:InterruptedException){
                            Thread.currentThread().interrupt()
                        }
                        thread=null

                    }
                }
                thread!!.start()
                num++
            }
        }
        btnThreadStop.setOnClickListener {
            if(thread!=null)
                thread!!.interrupt()
            thread=null
        }
        btnService.setOnClickListener {
            val intent= Intent(this,MyService::class.java)
            startService(intent)
        }
        btnServiceStop.setOnClickListener {
            val intent= Intent(this,MyService::class.java)
            stopService(intent)
        }
        btnIntentService.setOnClickListener {
            val intent= Intent(this,MyIntentService::class.java)
            startService(intent)
        }
        btnIntentServiceStop.setOnClickListener {
            val intent= Intent(this,MyIntentService::class.java)
            stopService(intent)
        }
    }
}
