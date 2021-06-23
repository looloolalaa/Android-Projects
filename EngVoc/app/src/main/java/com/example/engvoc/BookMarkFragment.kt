package com.example.engvoc

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.PrintStream
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class BookMarkFragment : Fragment(),
    VocFragment.OnListFragmentInteractionListener {

    var words2 = mutableMapOf<String, String>()  //영단어장
    var array2 = ArrayList<String>()  //리사이클러뷰에 표시할 배열

    //var listener: VocFragment.OnListFragmentInteractionListener?=null

    lateinit var adapter: MyAdapter
    lateinit var tts: TextToSpeech
    var isReady=false
    private var columnCount = 1

    var recycler:RecyclerView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_book_mark, container, false)

        readFile()


        // Set the adapter
        if (view is RecyclerView) {
            recycler=view
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyAdapter(array2,true)
                init(view)
            }
        }
        return view


    }

    override fun onResume() {
        super.onResume()
        updateAdapter()
        (recycler?.adapter as MyAdapter).notifyDataSetChanged()
    }

    fun updateAdapter(){

        readFile()
        init(recycler!!)
        (recycler?.adapter as MyAdapter).notifyDataSetChanged()
        Toast.makeText(this.context,"업데이트 되었습니다!",Toast.LENGTH_SHORT).show()

    }

    private fun init(view:RecyclerView) {
        tts = TextToSpeech(activity, TextToSpeech.OnInitListener {
            isReady=true
            tts.language = Locale.US
        })

        view.layoutManager=LinearLayoutManager(this.context,   //레이아웃매니저 설정
            LinearLayoutManager.VERTICAL, false)


        adapter=MyAdapter(array2,true)    //어답터 설정
        adapter.listener = object : MyAdapter.onItemClickListener{ //미구현이었던 온클릭이벤트 구현
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun itemClick(
                holder: MyAdapter.MyViewHolder,
                view: View,
                data: String,
                position: Int
            ) {
                //내용
                if(isReady)     //tts 읽기
                    tts.speak(data, TextToSpeech.QUEUE_ADD, null, null)

                //Toast.makeText(applicationContext, words[data],Toast.LENGTH_SHORT).show()
                //토스트 메세지 띄우기


                //VISIBLE, words[data]

                if(holder.meanView.visibility == View.GONE) {  //안보이면
                    holder.meanView.visibility = View.VISIBLE  //보이게 하고
                    holder.meanView.text = words2[data]  //영단어 뜻 넣기
                }else{ //보이면
                    // INVISIBLE
                    holder.meanView.visibility = View.GONE //안보이게 하기
                }


            }

        }




        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.DOWN or ItemTouchHelper.UP,
            ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }

        }

        adapter.listener2=object :MyAdapter.onCheckBoxListener{
            override fun ischecked(holder: MyAdapter.MyViewHolder, data: String, position: Int) {
//                bookmark[data]=words[data]!!
//                Toast.makeText(context, data+"추가되미!!",Toast.LENGTH_SHORT).show()
            }
            override fun isNotchecked(holder: MyAdapter.MyViewHolder, data: String, position: Int) {
                alertPopup(data)

            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(view)



        view.adapter=adapter //어답터 적용
        //(view.adapter as MyAdapter).notifyDataSetChanged()
    }

    fun alertPopup(data:String){
//        val args=Bundle()
//        args.putString("key","value")
//
//        val dialog = DialogAlert()
//        dialog.arguments=args
//        dialog.show(activity?.supportFragmentManager!!,"tag")

        var dialog=AlertDialog.Builder(this.context)
        dialog.setTitle("안녕하세요")
        dialog.setMessage(data+" 를 삭제하시겠습니까?")

        fun positiveFun(){
            array2.remove(data)
            Toast.makeText(context, data+" 가 삭제되었습니다!",Toast.LENGTH_SHORT).show()
            (recycler?.adapter as MyAdapter).notifyDataSetChanged()
        }

        var dialogListener=object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE->positiveFun()
                }
            }

        }

        dialog.setPositiveButton("확인",dialogListener)
        dialog.setNegativeButton("취소",dialogListener)
        dialog.show()

    }


    private fun scanFile(scan: Scanner) {  // 파일 읽어서 데이터 변수에 저장

        while(scan.hasNext()){
            val word = scan.nextLine()
            var mean:String=""
            if(words2[word]==null){
                if(scan.hasNext()) {
                    mean = scan.nextLine()
                }
                words2[word]=mean
                array2.add(word)
            }else{
                mean = scan.nextLine()
            }

        }
        scan.close()
    }

    fun readFile(){
        words2.clear()
        array2.clear()
        val output = PrintStream(this.context?.openFileOutput("bookmark1.txt", Context.MODE_APPEND))

        val scan2 = Scanner(this.context?.openFileInput("bookmark1.txt"))
        scanFile(scan2)

    }

    override fun onListFragmentInteraction(items: MutableMap<String, String>) {
        //nothing
    }


}
