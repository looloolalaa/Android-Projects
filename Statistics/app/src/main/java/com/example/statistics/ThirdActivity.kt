package com.example.statistics

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.JOYFUL_COLORS
import kotlinx.android.synthetic.main.activity_third.*
import kotlin.random.Random


class ThirdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        init()
    }

    fun init(){

        val i=intent
        val title=i.getStringExtra("title")
        val keys=i.getStringArrayListExtra("keys")
        val values=i.getStringArrayListExtra("values")


        var pieChart = piechartView

        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5F, 30F, 5F, 5F)
        pieChart.setDragDecelerationFrictionCoef(0.95f)
        pieChart.setDrawHoleEnabled(false)
        pieChart.setHoleColor(Color.WHITE)
        pieChart.setTransparentCircleRadius(61f)

        val yValues: ArrayList<PieEntry> = ArrayList<PieEntry>()

        for(i in 0 until keys.size){
            yValues.add(PieEntry(values[i].toFloat(),keys[i]))
        }

        titleView.setText(title+" 비율")
        val description = Description()
        description.setText("단위 : %") //라벨

        description.setTextSize(25F)
        pieChart.setDescription(description)

        pieChart.animateY(1000, Easing.EaseInOutCubic) //애니메이션


        val dataSet = PieDataSet(yValues, "(%)")
        dataSet.setSliceSpace(3f)
        dataSet.setSelectionShift(5f)

        var colors= mutableListOf<Int>()

        for(i in 0 until yValues.size){
            val r= Random.nextInt(255)
            val g= Random.nextInt(255)
            val b= Random.nextInt(255)
            val color=Color.rgb(r,g,b)
            colors.add(color)
        }
        dataSet.setColors(colors)

        val data = PieData(dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.YELLOW)

        if(!this.isFinishing)
            pieChart.setData(data)
    }
}
