package com.example.webtask

import android.opengl.Visibility
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    fun init(){
        button.setOnClickListener {
            val url= URL(editText.text.toString())
            Toast.makeText(this,url.toString(),Toast.LENGTH_SHORT).show()
            val myTask=MyAsyncTask(this)
            myTask.execute(url)
        }
    }

    class MyAsyncTask(context:MainActivity):AsyncTask<URL,Unit,String>(){
        
        private val activityreference=WeakReference(context) //아우터인 메인 액티비티 접근
        
        //시작 전 처음 시작
        override fun onPreExecute() {  //초기화 콜백 함수
            super.onPreExecute()
            val activity=activityreference.get()    //메인의
            activity?.progressBar?.visibility= View.VISIBLE   //progressbar 보여줘버리기
        }

        //실기능 콜백 함수
        override fun doInBackground(vararg params: URL?): String {      //들어온 url로 connect 하고 String 반환
            var result=""
            val connect=params[0]?.openConnection() as HttpURLConnection
            connect.connectTimeout=4000
            connect.readTimeout=4000
            connect.requestMethod="GET"
            connect.connect()
            val responseCode=connect.responseCode
            if(responseCode==200)
                result=streamToString(connect.inputStream)
            return result
        }

        //doInBackground 끝나고 호출되는 콜백 함수
        override fun onPostExecute(result: String?) {  //doInBackground 에서 반환한 String 을 인자로 받아버리미
            super.onPostExecute(result)
            val activity=activityreference.get()
            if(activity==null || activity.isFinishing)
                return
            activity.textView?.text=result
            activity.progressBar.visibility=View.GONE
        }

        //InputStream -> String
        fun streamToString(inputStream:InputStream):String{
            val bufferReader = BufferedReader(InputStreamReader(inputStream))
            var line:String
            var result=""
                try {
                    do {
                        line=bufferReader.readLine()
                        if(line!=null){
                            result +=line
                        }
                    }while (line!=null)
                    inputStream.close()
                }catch (ex:Exception){
                    Log.e("error","읽기 실패")
                }
            return result
        }
    }
}
