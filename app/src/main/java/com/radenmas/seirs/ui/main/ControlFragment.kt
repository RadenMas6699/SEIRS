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
import com.radenmas.seirs.databinding.FragmentControlBinding

class ControlFragment : Fragment() {

    private lateinit var b: FragmentControlBinding

    val dbKontrol = FirebaseDatabase.getInstance().getReference("kontrol")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentControlBinding.inflate(layoutInflater, container, false)
        val v = b.root

        initView()
        onClick()

        return v
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun onClick() {
        b.switchPumpIrrigation.setOnCheckedChangeListener { compoundButton, _ ->
            if (compoundButton.isChecked) {
                b.tvStatusPumpIrrigation.text = resources.getString(R.string.status, "ON")
                b.rlPumpIrrigation.background =
                    resources.getDrawable(R.drawable.bg_active, null)
                dbKontrol.child("pompaIrigasi").setValue(1)
            } else {
                b.tvStatusPumpIrrigation.text = resources.getString(R.string.status, "OFF")
                b.rlPumpIrrigation.background =
                    resources.getDrawable(R.drawable.bg_inactive, null)
                dbKontrol.child("pompaIrigasi").setValue(0)
            }
        }

        b.switchPumpPh.setOnCheckedChangeListener { compoundButton, _ ->
            if (compoundButton.isChecked) {
                b.tvStatusPumpPh.text = resources.getString(R.string.status, "ON")
                b.rlPumpPh.background =
                    resources.getDrawable(R.drawable.bg_active, null)
                dbKontrol.child("pompaPh").setValue(1)
            } else {
                b.tvStatusPumpPh.text = resources.getString(R.string.status, "OFF")
                b.rlPumpPh.background =
                    resources.getDrawable(R.drawable.bg_inactive, null)
                dbKontrol.child("pompaPh").setValue(0)
            }
        }

        b.switchPumpNutrisi.setOnCheckedChangeListener { compoundButton, _ ->
            if (compoundButton.isChecked) {
                b.tvStatusPumpNutrisi.text = resources.getString(R.string.status, "ON")
                b.rlPumpNutrisi.background =
                    resources.getDrawable(R.drawable.bg_active, null)
                dbKontrol.child("pompaNutrisi").setValue(1)
            } else {
                b.tvStatusPumpNutrisi.text = resources.getString(R.string.status, "OFF")
                b.rlPumpNutrisi.background =
                    resources.getDrawable(R.drawable.bg_inactive, null)
                dbKontrol.child("pompaNutrisi").setValue(0)
            }
        }

        b.switchPumpMix.setOnCheckedChangeListener { compoundButton, _ ->
            if (compoundButton.isChecked) {
                b.tvStatusPumpMix.text = resources.getString(R.string.status, "ON")
                b.rlPumpMix.background =
                    resources.getDrawable(R.drawable.bg_active, null)
                dbKontrol.child("pompaAduk").setValue(1)
            } else {
                b.tvStatusPumpMix.text = resources.getString(R.string.status, "OFF")
                b.rlPumpMix.background =
                    resources.getDrawable(R.drawable.bg_inactive, null)
                dbKontrol.child("pompaAduk").setValue(0)
            }
        }
    }

    private fun initView() {
        getStatusPump()
    }

    private fun getStatusPump() {
        FirebaseDatabase.getInstance().getReference("kontrol")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onDataChange(snapshot: DataSnapshot) {
                    val pompaIrigasi = snapshot.child("pompaIrigasi").value.toString().toInt()
                    val pompaPh = snapshot.child("pompaPh").value.toString().toInt()
                    val pompaNutrisi = snapshot.child("pompaNutrisi").value.toString().toInt()
                    val pompaAduk = snapshot.child("pompaAduk").value.toString().toInt()

                    if (pompaIrigasi == 1) {
                        b.switchPumpIrrigation.isChecked = true
                        b.tvStatusPumpIrrigation.text = resources.getString(R.string.status, "ON")
                        b.rlPumpIrrigation.background =
                            resources.getDrawable(R.drawable.bg_active, null)
                    } else {
                        b.switchPumpIrrigation.isChecked = false
                        b.tvStatusPumpIrrigation.text = resources.getString(R.string.status, "OFF")
                        b.rlPumpIrrigation.background =
                            resources.getDrawable(R.drawable.bg_inactive, null)
                    }

                    if (pompaPh == 1) {
                        b.switchPumpPh.isChecked = true
                        b.tvStatusPumpPh.text = resources.getString(R.string.status, "ON")
                        b.rlPumpPh.background = resources.getDrawable(R.drawable.bg_active, null)
                    } else {
                        b.switchPumpPh.isChecked = false
                        b.tvStatusPumpPh.text = resources.getString(R.string.status, "OFF")
                        b.rlPumpPh.background = resources.getDrawable(R.drawable.bg_inactive, null)
                    }

                    if (pompaNutrisi == 1) {
                        b.switchPumpNutrisi.isChecked = true
                        b.tvStatusPumpNutrisi.text = resources.getString(R.string.status, "ON")
                        b.rlPumpNutrisi.background =
                            resources.getDrawable(R.drawable.bg_active, null)
                    } else {
                        b.switchPumpNutrisi.isChecked = false
                        b.tvStatusPumpNutrisi.text = resources.getString(R.string.status, "OFF")
                        b.rlPumpNutrisi.background =
                            resources.getDrawable(R.drawable.bg_inactive, null)
                    }

                    if (pompaAduk == 1) {
                        b.switchPumpMix.isChecked = true
                        b.tvStatusPumpMix.text = resources.getString(R.string.status, "ON")
                        b.rlPumpMix.background = resources.getDrawable(R.drawable.bg_active, null)
                    } else {
                        b.switchPumpMix.isChecked = false
                        b.tvStatusPumpMix.text = resources.getString(R.string.status, "OFF")
                        b.rlPumpMix.background = resources.getDrawable(R.drawable.bg_inactive, null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}