package com.example.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_image.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(),ImageFragment.callImageListener,TextFragment.callTextListener{

    var imgNum=0
    var flag=true //이미지,텍스트 확인용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    fun init(){
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioBtn1 -> {
                    imgNum=0
                }
                R.id.radioBtn2 -> {
                    imgNum=1
                }
                R.id.radioBtn3 -> {
                    imgNum=2
                }
            }
            if(flag)  //이미지면
                makeImageFrag(imgNum)
            else  //텍스트면
                makeTextFrag(imgNum)
        }
        makeImageFrag(0)
    }

    fun makeImageFrag(num:Int){
        val fragment=supportFragmentManager.findFragmentById(R.id.frame)
        if(fragment==null){
            val imageTransaction=supportFragmentManager.beginTransaction()
            val imageFragment=ImageFragment.newImageFragment(num)
            imageTransaction.replace(R.id.frame,imageFragment)
            imageTransaction.commit()
            flag=true
        }else{
            val imageFragment=supportFragmentManager.findFragmentByTag("imgFrag")
            if(imageFragment==null){
                val imageTransaction=supportFragmentManager.beginTransaction()
                val imageFragment=ImageFragment.newImageFragment(num)
                imageTransaction.replace(R.id.frame,imageFragment)
                imageTransaction.commit()
                flag=true
            }else {
                (imageFragment as ImageFragment).setImageNum(num)
            }
        }
    }

    fun makeTextFrag(num:Int){
        val fragment=supportFragmentManager.findFragmentByTag("textFrag")
        if(fragment==null){
            val textTransaction=supportFragmentManager.beginTransaction()
            val textFragment=TextFragment.newTextFragment(num)
            textTransaction.replace(R.id.frame,textFragment,"textFrag")
            textTransaction.commit()
            flag=false
        }else{
            (fragment as TextFragment).setActiveImg(num)
        }
    }


    override fun chageTextFrag(num: Int) {
        makeTextFrag(num)
    }

    override fun chageImageFrag(num: Int) {
        makeImageFrag(num)
    }
}
