package com.example.servicemp3

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*

class MyService : Service() {

    lateinit var song:String
    var player:MediaPlayer?=null
    var binder=MyBinder()
    var manager:NotificationManager?=null
    var notificationid=100

    @RequiresApi(Build.VERSION_CODES.O)
    fun makeNotification(){
        val channel=NotificationChannel("channel1","mp3Channel",NotificationManager.IMPORTANCE_DEFAULT)
        manager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager?.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this,"channel1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("MP3")
            .setContentText("음악 플레이중")
        val notification=builder.build()
        startForeground(notificationid,notification)
    }

//    var receiver=object :BroadcastReceiver(){
//        override fun onReceive(context: Context?, intent: Intent?) {
//            val mode=intent?.getStringExtra("mode")
//            if(mode!=null){
//                when(mode){
//                    "start"->{
//                        song=intent.getStringExtra("song")
//                        val songid=resources.getIdentifier(song,"raw",packageName)
//                        if(player!=null && player!!.isPlaying){
//                            player!!.stop()
//                            player!!.reset()
//                            player!!.release()
//                            player=null
//                        }
//                        player=MediaPlayer.create(context,songid)
//                        player!!.start()
//                        val mainIntent=Intent("com.example.PLAY_TO_ACTIVITY")
//                        mainIntent.putExtra("mode","start")
//                        mainIntent.putExtra("duration",player!!.duration)
//                        sendBroadcast(mainIntent)
//                        player!!.setOnCompletionListener {
//                            val aIntent=Intent("com.example.PLAY_TO_ACTIVITY")
//                            aIntent.putExtra("mode","stop")
//                            sendBroadcast(aIntent)
//                        }
//                    }
//                    "stop"->{
//                        if(player!=null && player!!.isPlaying){
//                            player!!.stop()
//                            player!!.reset()
//                            player!!.release()
//                            player=null
//                        }
//                    }
//                }
//            }
//        }
//
//    }

//    override fun onCreate() {
//        super.onCreate()
//        registerReceiver(receiver, IntentFilter("com.example.PLAY_TO_SERVICE"))
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(receiver)
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //return super.onStartCommand(intent, flags, startId)
        Log.i("MyService","onStartCommand")
        when(intent?.action){
            "start"->{
                makeNotification()
            }
            "finish"->{
                stopForeground(true)
            }
            "play"->{
                song=intent.getStringExtra("song")
                val songid=resources.getIdentifier(song,"raw",packageName)
                if(player!=null && player!!.isPlaying){
                    player!!.stop()
                    player!!.reset()
                    player!!.release()
                    player=null
                }
                player=MediaPlayer.create(this,songid)
                player!!.start()

                player!!.setOnCompletionListener {
                    player!!.stop()
                    player!!.reset()
                    player!!.release()
                    player=null
                }
            }
            "stop"->{
                stopPlay()
            }
        }
        return START_STICKY
    }



    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    inner class MyBinder:Binder(){
        fun getService():MyService=this@MyService
    }

    fun getMaxDuration():Int{
        if(player!=null)
            return player!!.duration
        else
            return 0
    }

    fun startPlay(sname:String){
        song=sname
        val songid=resources.getIdentifier(song,"raw",packageName)
        if(player!=null && player!!.isPlaying){
            player!!.stop()
            player!!.reset()
            player!!.release()
            player=null
        }
        player=MediaPlayer.create(this,songid)
        player!!.start()

        player!!.setOnCompletionListener {
            player!!.stop()
            player!!.reset()
            player!!.release()
            player=null
        }

    }

    fun stopPlay(){
        if(player!=null && player!!.isPlaying){
            player!!.stop()
            player!!.reset()
            player!!.release()
            player=null
        }
    }
}
