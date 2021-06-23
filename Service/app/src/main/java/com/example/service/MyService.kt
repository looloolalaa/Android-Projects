package com.example.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

//다중 스레드
class MyService : Service() { //서비스는 백그라운드에서도 작업가능

    //var thread:Thread?=null
    var num=0

    override fun onCreate() {
        super.onCreate()
        Log.i("MyService","onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("MyService","onStartCommand")

        //if(thread==null){
            var thread=object :Thread("MyService"){
                val count=num
                override fun run() {
                    try {
                        for (i in 0..10){
                            Log.i("MyService+$count","count : $i")
                            Thread.sleep(1000)
                        }
                    }catch (e:InterruptedException){
                        Thread.currentThread().interrupt()
                    }
                    //thread=null

                }
            }
            thread.start()
            num++

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MyService","onDestroy")

//        if(thread!=null)
//            thread!!.interrupt()
//        thread=null
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
