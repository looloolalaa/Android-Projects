package com.example.engquiz

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var words = mutableMapOf<String, String>()
    var means = ArrayList<String>() //한글 뜻
    var added_words = mutableListOf<MyData>() //추가 단어

    var flag = true  // 버튼 활성/비활성 조건, 온클릭 이벤트 조건

    val allQuestion = 10  //문제수
    var question = 1  //현재 문제 번호
    var correctNum = 0  //맞은 문제 수
    var score = 0 //화면에 표시할 점수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData() //데이터베이스 초기화
        init()  //핸들러 달기

        play()  //랜덤으로 어답터 생성, 어답터 적용


    }


     fun play() {  //계속 반복될 함수

         show() //점수판

         val rand = Random()
         val num = rand.nextInt(means.size)  //8이면
         var answer = means[num]  //정답 = 8번째 뜻

         wordView.text = words[answer] //랜덤으로 단어 뿌리기


         var arrayMeans = ArrayList<String>()  // 어답터로 들어갈 다섯개의 랜덤 뜻들

         val range = getRange(num)  //6..10
         for(idx in range)  //6..10 까지의 뜻을 
             arrayMeans.add(means[idx])
         
         repeat(num%5){  //재배열하고 3번
             arrayMeans.add(arrayMeans[0])
             arrayMeans.removeAt(0)
         }

         var circleIdx = 0 //정답 인덱스 0..4중 하나
         if(num%5 in 0..2)
             circleIdx=2-num%5
         else
             circleIdx=2-num%5+5


         //만약 added_words 가 비어있지 않으면 텍스트, 보기 덮어쓰고 요소삭제
         if(!added_words.isEmpty()){
             val size=added_words.size
             val added_word=added_words[size-1].word
             val added_mean=added_words[size-1].mean
             answer=added_mean

             wordView.text=added_word
             arrayMeans[circleIdx]=added_mean

             added_words.removeAt(size-1)
         }


         var adapter: MyAdapter = MyAdapter(arrayMeans, circleIdx) //5개의 뜻, 정답정보 전달
         adapter.listener = object: MyAdapter.OnClickListener{
             override fun onClickFun(
                 holder: MyAdapter.MyViewHolder,
                 view: View,
                 data: String,
                 position: Int
             ) { //뷰홀더 눌리면
                 turnOnBtn()  //일단 버튼 활성화 후

                 if(data == answer && flag){ //클릭된 뷰홀더의 데이터가 정답이면
                     Toast.makeText(applicationContext, "맞았습니다!",Toast.LENGTH_SHORT).show()
                     correctNum++
                 }
                 if(data != answer && flag) //정답 아니면
                     Toast.makeText(applicationContext, "틀렸습니다..",Toast.LENGTH_SHORT).show()

                 if(flag) //정답이든 오답이든
                     adapter.circleHolder.textView.setBackgroundColor(Color.GRAY) //정답 뷰홀더 색상 변경
                 flag = false
             }

         }
         recyclerView.adapter = adapter //어답터에 대입


    }

    fun show(){
        if(question==allQuestion)
            nextBtn.text = "점수 확인"
        if(question>allQuestion) {
            question = 1
            score = 100*correctNum/allQuestion
            correctNum=0
        }
        var str = ""
        str+=question.toString()+'/'
        str+=allQuestion.toString()+"     "
        str+="점수: "+score.toString()
        testView.text = str
    } //점수판 보기


    fun turnOnBtn(){
        nextBtn.setEnabled(true)
    } //버튼 활성화

    fun turnOffBtn(){
        nextBtn.setEnabled(false)
    } //버튼 비활성화



    fun initData() {
        val scan2 = Scanner(openFileInput("added_words.txt"))
        while(scan2.hasNext()){
            val word= scan2.nextLine()
            val mean= scan2.nextLine()

            added_words.add(MyData(word,mean))
        }
        val scan = Scanner(resources.openRawResource(R.raw.words))
        while(scan.hasNext()){
            val word = scan.nextLine()
            val mean = scan.nextLine()
            words[mean] = word
            means.add(mean)
        }
        scan.close()
    } //데이터베이스 초기화

    fun getRange(x:Int):IntRange{
        val maxSize = means.size
        val range = when(x){
            0 or 1 -> 0..4
            maxSize-1 or maxSize -> maxSize-4..maxSize
            else -> x-2..x+2
        }
        return range
    } //랜덤 수 기준으로 -2..+2 5개 범위

    fun init(){

        nextBtn.setOnClickListener { //다음버튼이 눌릴 때 마다
            question++
            nextBtn.text = "다음"
            turnOffBtn() //버튼 비활성화하고

            play() //새로운 어답터 생성

            flag = true //버튼이 눌려야 flag 재설정
        }

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        //리사이클러의 레이아웃매니저 설정
        

    }


}
