package com.radenmas.seirs.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.radenmas.seirs.databinding.FragmentChartBinding
import java.text.SimpleDateFormat
import java.util.*

class ChartFragment : Fragment() {

    private lateinit var b: FragmentChartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentChartBinding.inflate(layoutInflater, container, false)
        val v = b.root

        initView()
        onClick()

        return v
    }

    private fun onClick() {

    }

    private fun initView() {
        getChart("suhuAir", b.chartTempWater)
        getChart("ph", b.chartPhWater)
        getChart("ppm", b.chartPpmWater)
    }

    fun chart(
        lineChart: LineChart,
        values: ArrayList<Entry>
    ) {
        val lineDataSet = LineDataSet(null, null)
        lineDataSet.values = values
//        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawFilled(true)
        lineDataSet.setDrawCircles(false)
//        lineDataSet.color = resources.getColor(R.color.green)
        lineDataSet.lineWidth = 1.5f
        val iLineDataSets = ArrayList<ILineDataSet>()
        iLineDataSets.add(lineDataSet)
        val lineData = LineData(iLineDataSets)

        val xAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.setCenterAxisLabels(true)
        xAxis.textSize = 8f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawLabels(true)

        xAxis.labelRotationAngle = 0f //45
        xAxis.setLabelCount(6, true)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val times = value * 1000
                val date = Date(times.toLong())
                val fmt = SimpleDateFormat("HH:mm")
                fmt.timeZone = TimeZone.getDefault()
                return fmt.format(date)
            }
        }

        val yAxisL = lineChart.getAxis(YAxis.AxisDependency.LEFT)
        yAxisL.setDrawGridLines(false)
        yAxisL.setDrawLabels(true)
        yAxisL.textSize = 8f

//        yAxisL.axisMinimum = 0f
//        yAxisL.axisMaximum = 1.5f

        lineChart.axisRight.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.description.isEnabled = false

        lineChart.notifyDataSetChanged()
        lineChart.data = lineData
        lineChart.invalidate()

        lineChart.moveViewTo(lineData.entryCount.toFloat(), 50f, YAxis.AxisDependency.LEFT)
    }

    private fun getChart(value: String, lineChart: LineChart) {
        val data = ArrayList<Entry>()
        data.clear()
        FirebaseDatabase.getInstance().getReference("dataPush").child("sensors").orderByKey()
            .limitToLast(300)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (snapshot1 in snapshot.children) {
                            val valueSensor = snapshot1.child(value).value.toString().toFloat()
                            val time = snapshot1.child("waktu").value.toString().toFloat()
                            data.add(Entry(time, valueSensor))
                        }
                        chart(lineChart, data)
                        lineChart.invalidate()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}