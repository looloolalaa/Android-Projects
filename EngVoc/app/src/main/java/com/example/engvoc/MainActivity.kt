package com.example.engvoc

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    //VocFragment.OnListFragmentInteractionListener,
    AddVocFragment.OnAddVocFragmentInteractionListener
    {

    val textArray= arrayListOf<String>("단어장","단어추가","즐겨찾기")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    fun init(){
        viewPager.adapter=MyFragStateAdapter(this)

        TabLayoutMediator(tabLayout,viewPager){
                tab, position ->
            tab.text=textArray[position]
        }.attach()
    }

//    override fun onListFragmentInteraction(items: MutableMap<String, String>) {
//        Toast.makeText(this,"",Toast.LENGTH_SHORT).show()
//    }

    override fun onAddVocFragmentInteraction() {
        viewPager.adapter?.notifyDataSetChanged()
    }


}
