package com.example.service

import android.app.IntentService
import android.content.Intent
import android.util.Log


//단일 스레드
class MyIntentService : IntentService("MyIntentService") { //서비스는 백그라운드에서도 작업가능

    companion object{
        var count=0
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("MyIntentService","onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MyIntentService","onDestroy")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("MyIntentService","onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.i("MyIntentService","onHandleIntent")
        val num=count++
        try {
            for (i in 0..10){
                Log.i("MyIntentService+$num","count : $i")
                Thread.sleep(1000)
            }
        }catch (e:InterruptedException){
            Thread.currentThread().interrupt()
        }
    }
}
