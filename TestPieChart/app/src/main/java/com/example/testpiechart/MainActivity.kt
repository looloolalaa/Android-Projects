package com.example.testpiechart

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    fun init(){
        var pieChart = piechartView

        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5F, 10F, 5F, 5F)

        pieChart.setDragDecelerationFrictionCoef(0.95f)

        pieChart.setDrawHoleEnabled(false)
        pieChart.setHoleColor(Color.WHITE)
        pieChart.setTransparentCircleRadius(61f)

        val yValues: ArrayList<PieEntry> = ArrayList<PieEntry>()

        yValues.add(PieEntry(34f, "Japen"))
        yValues.add(PieEntry(23f, "USA"))
        yValues.add(PieEntry(14f, "UK"))
        yValues.add(PieEntry(35f, "India"))
        yValues.add(PieEntry(40f, "Russia"))
        yValues.add(PieEntry(40f, "Korea"))

        val description = Description()
        description.setText("세계 국가") //라벨

        description.setTextSize(15F)
        pieChart.setDescription(description)

        //pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic) //애니메이션


        val dataSet = PieDataSet(yValues, "Countries")
        dataSet.setSliceSpace(3f)
        dataSet.setSelectionShift(5f)
        dataSet.setColors(0,1,2)

        val data = PieData(dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.YELLOW)

        if(!this.isFinishing)
            pieChart.setData(data)
    }
}
