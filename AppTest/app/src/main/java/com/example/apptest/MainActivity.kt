package com.example.apptest

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val APPLIST_REQUEST=100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            btnAction()
        }
    }

    private fun btnAction() {
        val i=Intent("com.example.myapplists")
        if(ActivityCompat.checkSelfPermission(this,
                        "com.example.applists")
                !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    arrayOf("com.example.applists"),APPLIST_REQUEST)
        }else{
            startActivity(i)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==APPLIST_REQUEST){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                btnAction()
            else
                finish()
        }
    }
}
