package com.example.pmtest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val CALL_REQUEST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    fun callAlertDlg(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("권한 허용").setMessage("반드시 CALL_PHONE 권한이 허용되어야 합니다.")
        builder.setPositiveButton("OK"){
            _,_ -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),CALL_REQUEST)
        }
        val dlg = builder.create()
        dlg.show()
    }

    fun callAction(){  //권한 있는지 확인하고 있으면 인텐트 실행 없으면 권한 요청
        val number = Uri.parse("tel:010-9144-8716")
        val callIntent= Intent(Intent.ACTION_CALL,number)

        if (ActivityCompat.checkSelfPermission(    //권한 확인
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED  // 승인되지 않았을 시
        ) {
            //권한 요청 창
            callAlertDlg()
        }else {       //승인되었을 시
            startActivity(callIntent)  // 확인했는데 승인이었으니 작업ㄱㄱ
        }
    }

    override fun onRequestPermissionsResult(       //권한 요청 결과
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CALL_REQUEST->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){       //'허용' 눌렀을 때 
                    Toast.makeText(this, "권한이 승인되었습니다.",Toast.LENGTH_SHORT).show()
                    callAction()
                }
                else    //'거부' 눌렀을 때
                    Toast.makeText(this, "권한 승인이 거부되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun init(){
        callBtn.setOnClickListener {
            callAction()
        }

        msgBtn.setOnClickListener {
            val message = Uri.parse("sms:010-9144-8716")
            val msgIntent= Intent(Intent.ACTION_SENDTO,message)
            msgIntent.putExtra("sms_body","천천히 가고 싶어요,,")
            startActivity(msgIntent)
        }

        webBtn.setOnClickListener {
            val webPage = Uri.parse("http://www.naver.com")
            val webIntent= Intent(Intent.ACTION_VIEW,webPage)
            startActivity(webIntent)
        }

        mapBtn.setOnClickListener {
            val location = Uri.parse("geo:37.543684,127.077130?z=16")
            val mapIntent= Intent(Intent.ACTION_VIEW,location)
            startActivity(mapIntent)
        }
    }

}
