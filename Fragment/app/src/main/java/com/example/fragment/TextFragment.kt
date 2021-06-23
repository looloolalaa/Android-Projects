package com.example.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_text.*
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class TextFragment : Fragment() {

    var imgNum=0
    val data= arrayListOf<String>("This is Blacknut's album","This is Leellamarz's album","This is Noel's album")

    interface callTextListener{
        fun chageImageFrag(num:Int)
    }

    companion object{
        fun newTextFragment(num:Int):TextFragment{
            val textFragment=TextFragment()
            textFragment.imgNum=num
            return textFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text, container, false)
    }

    fun setActiveImg(img:Int){
        imgNum=img
        textFragView.text=data[imgNum]
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textFragView.setOnClickListener {
            if(activity is callTextListener){
                val textFrag=activity as callTextListener
                textFrag.chageImageFrag(imgNum)
            }
        }
        setActiveImg(imgNum)
    }

    //        val i=requireActivity().intent
//        if(i!=null) {
//            val img = i.getIntExtra("imgNum", -1)
//            if(img!=-1)
//                setActiveImg(img)
//            else
//                setActiveImg(imgNum)
//        }

}
