package com.example.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_image.*

/**
 * A simple [Fragment] subclass.
 */
class ImageFragment : Fragment() {

    var imgNum=0

    interface callImageListener{
        fun chageTextFrag(num:Int)
    }

    companion object{
        fun newImageFragment(num:Int):ImageFragment{
            val imageFragment=ImageFragment()
            imageFragment.imgNum=num
            return imageFragment
        }
    }

    override fun onCreateView(            //뷰생성
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {       //뷰생성 완료시
        super.onActivityCreated(savedInstanceState)
        imageView.setOnClickListener {
            if(activity is callImageListener){
                val textFrag=activity as callImageListener
                textFrag.chageTextFrag(imgNum)
            }
        }
        setImageNum(imgNum)
    }

    fun setImageNum(num:Int){
        imgNum=num
        when(imgNum){
            0->imageView.setImageResource(R.drawable.blacknut)
            1->imageView.setImageResource(R.drawable.leellamarz)
            2->imageView.setImageResource(R.drawable.noel)
        }
    }

    //        radioGroup.setOnCheckedChangeListener { group, checkedId ->
//            when(checkedId){
//                R.id.radioBtn1-> {
//                    imageView.setImageResource(R.drawable.blacknut)
//                    imgNum=1
//                }
//                R.id.radioBtn2-> {
//                    imageView.setImageResource(R.drawable.leellamarz)
//                    imgNum=2
//                }
//                R.id.radioBtn3-> {
//                    imageView.setImageResource(R.drawable.noel)
//                    imgNum=3
//                }
//            }
//            checkOrientation()

//        imageView.setOnClickListener {
//            if(resources.configuration.orientation==Configuration.ORIENTATION_PORTRAIT){
//                val i=Intent(activity,SecondActivity::class.java)
//                i.putExtra("imgNum",imgNum)
//                startActivity(i)
//            }else if(resources.configuration.orientation==Configuration.ORIENTATION_LANDSCAPE){
//                val txtFrag=requireActivity().textFragment as TextFragment
//                txtFrag.setActiveImg(imgNum)
//            }
//        }
//    }
//    fun checkOrientation(){
//        if(resources.configuration.orientation==Configuration.ORIENTATION_LANDSCAPE){
//            val txtFrag=requireActivity().textFragment as TextFragment
//            txtFrag.setActiveImg(imgNum)
//        }
//    }

}
