package com.example.statistics

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.jsoup.Jsoup
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.collections.MutableMap
import kotlin.collections.mutableMapOf
import kotlin.collections.set

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    fun init(){

        startJSONTask()
    }



    fun startJSONTask(){
        val task=MyAsyncTask(this)
        task.execute(
            //list URL
            //영유아
            URL("http://kosis.kr/openapi/statisticsList.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&vwCd=MT_TM1_TITLE&parentListId=101_A0103&format=json&jsonVD=Y")
            //아동
            ,URL("http://kosis.kr/openapi/statisticsList.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&vwCd=MT_TM1_TITLE&parentListId=A0204&format=json&jsonVD=Y")
            //학생
            ,URL("http://kosis.kr/openapi/statisticsList.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&vwCd=MT_TM1_TITLE&parentListId=A04_A3&format=json&jsonVD=Y")
            //노인
            ,URL("http://kosis.kr/openapi/statisticsList.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&vwCd=MT_TM1_TITLE&parentListId=101_A0502&format=json&jsonVD=Y")
            //외국인
            //,URL("http://kosis.kr/openapi/statisticsList.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&vwCd=MT_TM1_TITLE&parentListId=A09_A09_4&format=json&jsonVD=Y")
            //다문화
            //,URL("http://kosis.kr/openapi/statisticsList.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&vwCd=MT_TM1_TITLE&parentListId=101_A10_004&format=json&jsonVD=Y")

            //table URL
            //영유아
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/101/DT_1IN1503/2/1/20200619220208&prdSe=Y&newEstPrdCnt=1")
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/101/DT_1IN0001/2/1/20200620202640&prdSe=Y&newEstPrdCnt=1")
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/101/DT_1BPA001/2/1/20200620202939&prdSe=Y&newEstPrdCnt=1")
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/101/DT_1BPB001/2/1/20200620203052&prdSe=Y&newEstPrdCnt=1")

            //아동
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/117/TX_117_2009_H7001/2/1/20200619220617&prdSe=Y&newEstPrdCnt=1")
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/117/TX_117_2009_H7002/2/1/20200620204545&prdSe=Y&newEstPrdCnt=1")
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/117/TX_117341138/2/1/20200620204703&prdSe=Y&newEstPrdCnt=1")
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/117/DT_11770_N005/2/1/20200620204746&prdSe=Y&newEstPrdCnt=1")
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/117/TX_117401121/2/1/20200620204818&prdSe=Y&newEstPrdCnt=1")

            //학생
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/101/DT_1YL8801/2/1/20200620205644&prdSe=Y&newEstPrdCnt=1")
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/101/DT_1SSED030R/2/1/20200620210004&prdSe=Y&newEstPrdCnt=1")
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/101/DT_1SSED050R/2/1/20200620210214&prdSe=Y&newEstPrdCnt=1")

            //노인
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/101/DT_2KAA202/2/1/20200622204730&prdSe=Y&newEstPrdCnt=1")
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/101/DT_1BPA002/2/1/20200622205222&prdSe=Y&newEstPrdCnt=1")
            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/101/DT_1BPB002/2/1/20200622205317&prdSe=Y&newEstPrdCnt=1")

            //외국인
//            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/110/TX_11025_A005_A/2/1/20200623210241&prdSe=Y&newEstPrdCnt=1")
//            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/110/TX_11025_A006_A/2/1/20200623210415&prdSe=Y&newEstPrdCnt=1")
//            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/110/TX_11025_A007_A/2/1/20200623211351&prdSe=Y&newEstPrdCnt=1")

            //다문화
//            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/154/DT_MOGE_1001301398/2/1/20200622215155&prdSe=Y&newEstPrdCnt=1")
//            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/154/DT_MOGE_1001301406/2/1/20200623171801&prdSe=Y&newEstPrdCnt=1")
//            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/154/DT_MOGE_1001301416/2/1/20200622215841&prdSe=Y&newEstPrdCnt=1")
//            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/154/DT_MOGE_1001301421/2/1/20200622220032&prdSe=Y&newEstPrdCnt=1")
//            ,URL("http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=N2U2YjhlNjY3OTQ0ODgxZDE2NTUxNjMwZGE1OGFmZTc=&format=json&jsonVD=Y&userStatsId=dlrlgus65/154/DT_MOGE_1001301430/2/1/20200622220131&prdSe=Y&newEstPrdCnt=1")


        )
    }




    class MyAsyncTask(context:MainActivity):AsyncTask<URL,Unit,Unit>(){

        var tableJSONMap:MutableMap<String,String> = mutableMapOf()

        val activityReference=WeakReference(context)
        override fun doInBackground(vararg params: URL?) {
            val activity=activityReference.get()

            val listNum=4 //리스트 개수

            //테이블 삽입
            for(i in listNum until params.size){
                val doc = Jsoup.connect(params[i].toString()).ignoreContentType(true).get()
                val json = JSONArray(doc.text())
                val jObject = json.getJSONObject(0)
                val tableId=jObject["TBL_ID"].toString()

                Log.i("Here!!!!",tableId+": "+json.toString())

                tableJSONMap[tableId]=json.toString()
            }





            //리스트 생성
            for(i in 0..listNum-1) {
                val root = TreeNode("Root")
                val doc = Jsoup.connect(params[i].toString()).ignoreContentType(true).get()
                val json = JSONArray(doc.text())

                val title=when(i){
                    0->"영유아"
                    1->"아동"
                    2->"학생"
                    3->"노인"
                    //4->"외국인"
                    //5->"다문화"
                    else->"error"
                }
                val nodeItem=MyHolder.IconTreeItem()
                nodeItem.text=" "+ title
                nodeItem.icon=R.drawable.ic_folder_black_24dp
                val parent=TreeNode(nodeItem).setViewHolder(MyHolder(activity!!.applicationContext))
                //val parent = TreeNode("+ "+ title)

                for (j in 0 until json.length()) {
                    val jObject = json.getJSONObject(j)
                    val tableId=jObject["TBL_ID"].toString()
                    val tableName=jObject["TBL_NM"].toString()

                    val nodeItem=MyHolder.IconTreeItem()
                    nodeItem.text="    - "+tableName
                    nodeItem.icon=R.drawable.ic_format_list_bulleted_black_24dp
                    var child= TreeNode(nodeItem).setViewHolder(MyHolder(activity!!.applicationContext))
                    //child= TreeNode("   - "+tableName)

                    child.setClickListener { node, value ->
                        Toast.makeText(activity,tableId,Toast.LENGTH_SHORT).show()
                        val intent=Intent(activity,SecondActivity::class.java)
                        intent.putExtra("json",tableJSONMap[tableId])
                        activity?.startActivity(intent)
                    }
                    parent.addChild(child)
                }

                root.addChild(parent)
                val tView = AndroidTreeView(activity, root)
                activity?.runOnUiThread(Runnable {
                    // Stuff that updates the UI
                    activity?.scrollView?.addView(tView.getView())
                })
            }




        }


        override fun onPostExecute(result: Unit?) {      //외부 문서를 다 가져왔는데 메인이 끝났거나 끝나는 중이면 return
            super.onPostExecute(result)

            val activity=activityReference.get()
            if(activity==null || activity.isFinishing) {
                return
            }


        }
    }

    class MyHolder(context: Context) : TreeNode.BaseNodeViewHolder<MyHolder.IconTreeItem>(context) {
        @Override
        override fun createNodeView(node: TreeNode?, value: IconTreeItem?): View {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.row, null, false)
            var tvIcon=view.findViewById(R.id.icon) as ImageView
            val tvValue = view.findViewById(R.id.name) as TextView
            tvIcon.setImageResource(value!!.icon)
            tvValue.setText(value!!.text)


            return view
        }

        class IconTreeItem {
            var icon:Int = 0
            var text:String=""
        }
    }

}
