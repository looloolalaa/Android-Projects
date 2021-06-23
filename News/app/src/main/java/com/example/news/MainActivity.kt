package com.example.news

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.lang.ref.WeakReference
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        startJSONTask()
    }

    //JSON
    fun startJSONTask(){
        val task=MyAsyncTask(this)
        task.execute(URL("http://api.icndb.com/jokes/random"))
    }

    //XML
    fun startXMLTask(){
        val task=MyAsyncTask(this)
        task.execute(URL("http://fs.jtbc.joins.com//RSS/culture.xml"))
    }

    //HTML
    fun startTask(){  //daum.net 데이터 가져와서 recycler에 추가
        val task=MyAsyncTask(this)
        task.execute(URL("https://www.daum.net"))
    }

    fun init(){

        swiperefresh.setOnRefreshListener {  //업뎃
            swiperefresh.isRefreshing=true
            startTask()  //재실행
        }


        //타이틀 클릭 시 해당 url로 이동하는 이벤트 처리
        recyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
        adapter= MyAdapter(ArrayList<MyData>())
        adapter.itemClickListener=object:MyAdapter.OnItemClickListener{
            override fun OnItemClick(
                holder: MyAdapter.MyViewHolder,
                view: View,
                data: MyData,
                position: Int
            ) {
                val i=Intent(ACTION_VIEW,Uri.parse(data.url))
                startActivity(i)
            }

        }
        recyclerView.adapter=adapter
    }


    class MyAsyncTask(context:MainActivity):AsyncTask<URL,Unit,Unit>(){       //외부 웹사이트 연결 클래스!!!!

        val activityReference=WeakReference(context)

        override fun doInBackground(vararg params: URL?) {   //외부 문서 가져와서 어답터에 추가
            val activity=activityReference.get()
            activity?.adapter?.items?.clear()


            //JSON
            val doc=Jsoup.connect(params[0].toString()).ignoreContentType(true).get()
            //Log.i("JSON: ",doc.text())
            val json=JSONObject(doc.text())
            val joke=json.getJSONObject("value")
            val jokestr = joke.getString("joke")
            Log.i("JSON Joke: ",jokestr)

            //XML
//            val doc=Jsoup.connect(params[0].toString()).parser(Parser.xmlParser()).get()
//            val headlines = doc.select("item")
//            for(news in headlines){
//                activity?.adapter?.items?.add(MyData(news.select("title").text(),news.select("link").text()))
//            }


            //HTML
//            val doc=Jsoup.connect(params[0].toString()).get()
//            val headlines = doc.select("ul.list_txt>li>a")
//            for(news in headlines){
//                activity?.adapter?.items?.add(MyData(news.text(),news.absUrl("href")))
//            }
        }

        override fun onPostExecute(result: Unit?) {      //외부 문서를 다 가져왔는데 메인이 끝났거나 끝나는 중이면 return
            super.onPostExecute(result)                  //아니면 notify changed

            val activity=activityReference.get()
            if(activity==null || activity.isFinishing) {
                return
            }

            activity.swiperefresh.isRefreshing=false
            activity.adapter.notifyDataSetChanged()

        }
    }

}
