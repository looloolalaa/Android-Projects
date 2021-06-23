package com.example.dynamicfrag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_image.*

class MainActivity : AppCompatActivity(), SongFragment.OnListFragmentInteractionListener {

    val textArray= arrayListOf<String>("이미지","리스트")

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

    override fun onListFragmentInteraction(item: String?) {
        Toast.makeText(this,item,Toast.LENGTH_SHORT).show()
    }
}
