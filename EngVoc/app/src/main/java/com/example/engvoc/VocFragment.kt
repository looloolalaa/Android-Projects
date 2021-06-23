package com.example.engvoc

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_voc.*
import java.io.File
import java.io.FileReader
import java.io.PrintStream
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

open class VocFragment : Fragment() {

    var words = mutableMapOf<String, String>()  //영단어장
    var array = ArrayList<String>()  //리사이클러뷰에 표시할 배열

    var bookmark=mutableMapOf<String, String>()

    lateinit var adapter: MyAdapter
    lateinit var tts: TextToSpeech
    var isReady=false

    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null

    var recycler:RecyclerView?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_voc, container, false)

        readFile()


        // Set the adapter
        if (view is RecyclerView) {
            recycler=view
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyAdapter(array,false)
                init(view)
            }
        }
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is OnListFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
//        }

    }

    override fun onResume() {
        super.onResume()


        Toast.makeText(this.context,"업데이트 되었습니다!",Toast.LENGTH_SHORT).show()
        updateAdapter()

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onStop() {
        super.onStop()
        tts.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }

    fun updateAdapter(){

        readFile()
        init(recycler!!)
//        adapter=MyAdapter(array,false)
//        recycler?.adapter=adapter
        (recycler?.adapter as MyAdapter).notifyDataSetChanged()

    }

    private fun init(view:RecyclerView) {

        tts = TextToSpeech(activity, TextToSpeech.OnInitListener {
            isReady=true
            tts.language = Locale.US
        })

        view.layoutManager=LinearLayoutManager(this.context,   //레이아웃매니저 설정
            LinearLayoutManager.VERTICAL, false)


        adapter=MyAdapter(array,false)    //어답터 설정
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
                    holder.meanView.text = words[data]  //영단어 뜻 넣기 
                }else{ //보이면
                    // INVISIBLE
                    holder.meanView.visibility = View.GONE //안보이게 하기
                }


            }

        }
        adapter.listener2=object :MyAdapter.onCheckBoxListener{
            override fun ischecked(holder: MyAdapter.MyViewHolder, data: String, position: Int) {
                bookmark[data]=words[data]!!
                Toast.makeText(context, data+" 가 즐겨찾기에 추가되었습니다!",Toast.LENGTH_SHORT).show()
            }
            override fun isNotchecked(holder: MyAdapter.MyViewHolder, data: String, position: Int) {
                bookmark.remove(data)
                Toast.makeText(context, data+" 가 즐겨찾기에서 삭제되었습니다!",Toast.LENGTH_SHORT).show()
            }

        }


        //(view.adapter as MyAdapter).notifyDataSetChanged()
        

        val simpleCallback = object :ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP,
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
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(view)

        view.adapter=adapter //어답터 적용
    }


    private fun scanFile(scan: Scanner) {  // 파일 읽어서 데이터 변수에 저장

        while(scan.hasNext()){
            val word = scan.nextLine()
            var mean:String=""
            if(scan.hasNext()) {
                mean = scan.nextLine()
            }
            words[word]=mean
            array.add(word)
        }
        scan.close()
    }

    open fun readFile(){
        array.clear()
        val output = PrintStream(this.context?.openFileOutput("out3.txt", Context.MODE_APPEND))

        val scan2 = Scanner(this.context?.openFileInput("out3.txt"))
        scanFile(scan2)

        val scan = Scanner(resources.openRawResource(R.raw.words))
        scanFile(scan)

    }

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(items:MutableMap<String,String>)
    }

    override fun onPause() {
        super.onPause()
//        for(k in bookmark.keys)
//            Toast.makeText(this.context,k,Toast.LENGTH_SHORT).show()

        //Toast.makeText(this.context,bookmark.size.toString(),Toast.LENGTH_SHORT).show()

        //listener?.onListFragmentInteraction(bookmark)

        writeFile(bookmark)

    }

    private fun writeFile(items: MutableMap<String, String>) {

        val output = PrintStream(activity?.openFileOutput("bookmark1.txt", Context.MODE_APPEND))

        for(k in items.keys) {
            output.println(k)
            output.println(items[k])
        }
        output.close()

    }
}
