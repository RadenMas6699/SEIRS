package com.radenmas.seirs.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.radenmas.seirs.R
import com.radenmas.seirs.databinding.FragmentHidroponikBinding
import com.radenmas.seirs.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class HidroponikFragment : Fragment() {

    private lateinit var b: FragmentHidroponikBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentHidroponikBinding.inflate(layoutInflater, container, false)
        val v = b.root

        initView()
        onClick()

        return v
    }

    private fun onClick() {

    }

    private fun initView() {
        val dt = Date()
        when (dt.hours) {
            in 0..5 -> {
                b.tvGreeting.text = resources.getString(R.string.greeting, "Malam")
                b.imgGreeting.setImageResource(R.drawable.ic_night)
            }
            in 6..10 -> {
                b.tvGreeting.text = resources.getString(R.string.greeting, "Pagi")
                b.imgGreeting.setImageResource(R.drawable.ic_sunrise)
            }
            in 11..14 -> {
                b.tvGreeting.text = resources.getString(R.string.greeting, "Siang")
                b.imgGreeting.setImageResource(R.drawable.ic_sun)
            }
            in 15..18 -> {
                b.tvGreeting.text = resources.getString(R.string.greeting, "Sore")
                b.imgGreeting.setImageResource(R.drawable.ic_sunset)
            }
            in 19..23 -> {
                b.tvGreeting.text = resources.getString(R.string.greeting, "Malam")
                b.imgGreeting.setImageResource(R.drawable.ic_night)
            }
        }

        getDataRealtime()
    }

    private fun getDataRealtime() {
        FirebaseDatabase.getInstance().getReference("realtime")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val ph = snapshot.child("ph").value.toString().toFloat()
                        val ppm = snapshot.child("ppm").value
                        val suhuAir = snapshot.child("suhuAir").value.toString().toFloat()
                        val levelAir = snapshot.child("levelAir").value
                        val suhuGH = snapshot.child("suhuGh").value.toString().toFloat()
                        val humGH = snapshot.child("humGh").value.toString().toFloat()
                        val suhuPanel = snapshot.child("suhuPanel").value.toString().toFloat()
                        val humPanel = snapshot.child("humPanel").value.toString().toFloat()
                        val time = snapshot.child("time").value.toString().toLong()

                        b.tvPhWater.text = Utils.formatComma2(ph)
                        b.tvPpmWater.text = "$ppm"
                        b.tvTempWater.text = Utils.formatComma0(suhuAir) + "℃"
                        b.tvLevelWater.text = "$levelAir%"
                        b.tvTempGreenHouse.text = Utils.formatComma0(suhuGH) + "℃"
                        b.tvHumGreenHouse.text = Utils.formatComma0(humGH) + "%"
                        b.tvTempPanel.text = Utils.formatComma0(suhuPanel) + "℃"
                        b.tvHumPanel.text = Utils.formatComma0(humPanel) + "%"

                        val date = Date(time * 1000)
                        val formatDate = SimpleDateFormat("HH:mm zz\nEEEE, dd MMM yyyy")

                        b.tvLastUpdate.text = formatDate.format(date)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}