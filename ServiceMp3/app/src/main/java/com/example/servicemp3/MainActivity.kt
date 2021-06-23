package com.example.servicemp3

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var songlist:Array<String>
    lateinit var song:String
//    var runThread=false
//    var thread:ProgressThread?=null
//    lateinit var myService:MyService
//    var mBound=false

//    val connection=object :ServiceConnection{
//        override fun onServiceDisconnected(name: ComponentName?) {
//            mBound=false
//        }
//
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            mBound=true
//            val binder=service as MyService.MyBinder
//            myService=binder.getService()
//        }
//
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if(mBound)
//            unbindService(connection)
//        mBound=false
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    fun startPlay(){
//        val serviceBRIntent=Intent("com.example.PLAY_TO_SERVICE")
//        serviceBRIntent.putExtra("mode","start")
//        serviceBRIntent.putExtra("song",song)
//        sendBroadcast(serviceBRIntent)

//        runThread=true
//        if(thread==null|| !thread!!.isAlive){
//            if(mBound){
//                myService.startPlay(song)
//                progressBar.progress=0
//                progressBar.max=myService.getMaxDuration()
//                thread=ProgressThread()
//                thread!!.start()
//            }
//            thread=ProgressThread()
//            //progressBar.max=10000
//            thread!!.start()
//        }else{
//            if(mBound){
//                myService.stopPlay()
//                myService.startPlay(song)
//                progressBar.progress=0
//                progressBar.max=myService.getMaxDuration()
//            }
//        }

        val serviceIntent=Intent(this,MyService::class.java)
        serviceIntent.action="play"
        serviceIntent.putExtra("song",song)
        startService(serviceIntent)
    }

    fun stopPlay(){
//        val serviceBRIntent=Intent("com.example.PLAY_TO_SERVICE")
//        serviceBRIntent.putExtra("mode","stop")
//        sendBroadcast(serviceBRIntent)

//        if(mBound){
//            myService.stopPlay()
//        }
//        runThread=false
//        progressBar.progress=0

        val serviceIntent=Intent(this,MyService::class.java)
        serviceIntent.action="stop"
        startService(serviceIntent)
    }

    fun init(){
        songlist=resources.getStringArray(R.array.songlist)
        song=songlist[0]
        
        listView.setOnItemClickListener { parent, view, position, id ->
            song=songlist[position]
            startPlay()
        }
        btnPlay.setOnClickListener {
            startPlay()
        }
        btnStop.setOnClickListener {
            stopPlay()
        }
        serviceStartBtn.setOnClickListener {
            val serviceIntent=Intent(this,MyService::class.java)
            serviceIntent.action="start"
            startService(serviceIntent)
        }
        serviceStopBtn.setOnClickListener {
            val serviceIntent=Intent(this,MyService::class.java)
            serviceIntent.action="finish"
            startService(serviceIntent)
        }
//        registerReceiver(receiver, IntentFilter("com.example.PLAY_TO_ACTIVITY"))
//        val serviceIntent=Intent(this, MyService::class.java)
//        startService(serviceIntent)

//        val intent=Intent(this,MyService::class.java)
//        bindService(intent,connection,Context.BIND_AUTO_CREATE)

    }

//    inner class ProgressThread:Thread(){
//        override fun run() {
//            while (runThread){
//                progressBar.incrementProgressBy(1000)
//                SystemClock.sleep(1000)
//                if(progressBar.progress==progressBar.max) {
//                    runThread = false
//                    progressBar.progress=0
//                }
//            }
//        }
//    }
//
//    var receiver:BroadcastReceiver=object :BroadcastReceiver(){
//        override fun onReceive(context: Context?, intent: Intent?) {
//            val mode=intent?.getStringExtra("mode")
//            if(mode=="start"){
//                runThread=true
//                progressBar.max=intent.getIntExtra("duration",-1)
//                progressBar.progress=0
//            }else if(mode=="stop"){
//                runThread=false
//                progressBar.progress=0
//            }
//        }
//    }

}
