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
import com.radenmas.seirs.databinding.FragmentListrikBinding
import com.radenmas.seirs.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class ListrikFragment : Fragment() {

    private lateinit var b: FragmentListrikBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentListrikBinding.inflate(layoutInflater, container, false)
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
        FirebaseDatabase.getInstance().getReference("Power")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val voltAc = snapshot.child("voltAc").value
                        val arusAc = snapshot.child("arusAc").value
                        val dayaAc = snapshot.child("dayaAc").value
                        val energiAc = snapshot.child("energiAc").value
                        val freqAc = snapshot.child("freqAc").value
                        val pFactor = snapshot.child("pFactor").value
                        val vBatt = snapshot.child("vBatt").value
                        val vPanel = snapshot.child("vPanel").value
                        val time = snapshot.child("time").value.toString().toLong()

                        b.tvVoltAc.text = "${Utils.formatComma0(voltAc.toString().toFloat())}V"
                        b.tvArusAc.text = "${Utils.formatComma2(arusAc.toString().toFloat())}A"
                        b.tvDayaAc.text = "${dayaAc}W"
                        b.tvEnergiAc.text = "${Utils.formatComma1(energiAc.toString().toFloat())}kWh"
                        b.tvFrequensiAc.text = "${Utils.formatComma0(freqAc.toString().toFloat())}Hz"
                        b.tvPFactor.text = "${pFactor}Cos φ"
                        b.tvVoltBattery.text = "${Utils.formatComma1(vBatt.toString().toFloat())}V"
                        b.tvVoltPanel.text = "${Utils.formatComma1(vPanel.toString().toFloat())}V"

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