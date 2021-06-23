package com.example.pendingintent

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.mytimepicker_dialog.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var mymemo=""
    var myhour=0
    var mymin=0
    var message=""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //앱시작동시에 들어온 인텐트 있나 검사
        val str=intent.getStringExtra("time")  //인텐트 정보 받아버리기
        if(str!=null)  //null 이면 인텐트가 들어온게 없으니 아무것도
            Toast.makeText(this,str,Toast.LENGTH_SHORT).show()


        init()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun init(){

        //캘린더 클릭시 다이얼로그 띄우는 이벤트
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            //미리만들어놓은 레이아웃으로 다이얼로그 생성하기
            val dialog=layoutInflater.inflate(R.layout.mytimepicker_dialog,null)
            val dlgTime=dialog.findViewById<TimePicker>(R.id.timePicker)
            val dlgMemo=dialog.findViewById<EditText>(R.id.editText)
            val dlgBuilder=AlertDialog.Builder(this)

            dlgBuilder.setView(dialog)  //다이얼로그
                .setPositiveButton("추가"){ //추가버튼클릭시 토스트띄우고 2초뒤 알람
                    _,_ ->
                    mymemo=dlgMemo.text.toString()
                    myhour=dlgTime.hour
                    mymin=dlgTime.minute
                    message=myhour.toString()+"시 "+mymin.toString()+"분 :"+ mymemo
                    val timerTask=object :TimerTask(){
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun run() {
                            makeNotification() //알람
                        }
                    }
                    val timer=Timer()
                    timer.schedule(timerTask,2000)  //그 알람을 2초뒤 실행
                    Toast.makeText(this,"알림이 추가되었습니다.",Toast.LENGTH_SHORT).show()

                }
                .setNegativeButton("취소"){
                    _,_ ->

                }
                .show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun makeNotification(){ //알람 (채널+빌드+매니저)
        //1. 채널 - 알람 디테일
        val channelId="MyChannel"
        val channelName="TimeCheckChannel"
        val notificationChannel=NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableVibration(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor=Color.BLUE
        notificationChannel.lockscreenVisibility= Notification.VISIBILITY_PRIVATE

        //2. 빌드  (채널 정보 필요) - 알람 아이콘, 타이틀 ,텍스트 등 + !!!! 펜딩인텐트 소유 !!!!
        val builder=NotificationCompat.Builder(this,channelId)
            .setSmallIcon(R.drawable.ic_av_timer_black_24dp)
            .setContentTitle("일정 알림")
            .setContentText(message)
            .setAutoCancel(true)

        //펜딩인텐트에 인자로 들어갈 그냥인텐트
        val i=Intent(this,MainActivity::class.java)
        i.putExtra("time",message)
        i.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        //빌드에 펜딩인텐트 적용
        val pendingIntent=PendingIntent.getActivity(this,1,i,PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)
        //알람이 가지고 있는 펜딩인텐트 정보는 빌더가 갖고있다.

        //3. 매니저 - 실행  (채널,빌드 적용하고 실행)
        val manager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
        val notification=builder.build()
        manager.notify(10,notification) //최종 알람 띄우기

    }
}
