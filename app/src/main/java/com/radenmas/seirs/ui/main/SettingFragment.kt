package com.radenmas.seirs.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.radenmas.seirs.R
import com.radenmas.seirs.adapter.FirebaseViewHolder
import com.radenmas.seirs.databinding.BottomCreateFormulaBinding
import com.radenmas.seirs.databinding.FragmentSettingBinding
import com.radenmas.seirs.model.Formula
import com.radenmas.seirs.utils.Utils

class SettingFragment : Fragment() {

    private lateinit var b: FragmentSettingBinding
    private lateinit var bs: BottomCreateFormulaBinding
    private lateinit var dialog: BottomSheetDialog

    lateinit var reffFormula: DatabaseReference

    lateinit var options: FirebaseRecyclerOptions<Formula>
    lateinit var adapter: FirebaseRecyclerAdapter<Formula, FirebaseViewHolder>

    private val RESULT_OK = -1
    private var filePath: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentSettingBinding.inflate(layoutInflater, container, false)
        val v = b.root

        initView()
        onClick()

        return v
    }

    private fun onClick() {
        b.imgAdd.setOnClickListener {
            bs = BottomCreateFormulaBinding.inflate(layoutInflater)
            val v = bs.root

            dialog = BottomSheetDialog(requireActivity(), R.style.DialogStyle)
            dialog.setCancelable(true)
            dialog.setContentView(v)
            dialog.show()

            bs.imgDismiss.setOnClickListener {
                dialog.dismiss()
            }
//            bs.btnChooseImage.setOnClickListener {
//                val intent = Intent()
//                intent.type = "image/*"
//                intent.action = Intent.ACTION_GET_CONTENT
//                startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 71)
//            }

            bs.btnAddFormula.setOnClickListener {
                val name = bs.etNameFormula.text.trim().toString()
                val ph = bs.etValPh.text.toString().toFloat()
                val ppm = bs.etValPpm.text.toString().toInt()
                val id = reffFormula.push().key.toString()

//                if (filePath != null) {
                Utils.showLoading(requireContext())
//                    val storageReference =
//                        FirebaseStorage.getInstance().getReference("formula").child(id)
//                    val ref = storageReference.child(id)
//                    ref.putFile(filePath!!)
//                        .addOnSuccessListener {
//                            Utils.dismissLoading()
//                            ref.downloadUrl
//                                .addOnSuccessListener { uri: Uri ->
                val formula = Formula(id, name, ph, ppm)
                reffFormula.child(id).setValue(formula).addOnSuccessListener {
                    bs.etNameFormula.text.clear()
                    bs.etValPh.text.clear()
                    bs.etValPpm.text.clear()

                    dialog.dismiss()

                    Utils.dismissLoading()

                    Utils.toast(
                        requireContext(),
                        "Formula baru berhasil ditambahkan"
                    )
                }

//                                }
//                        }.addOnFailureListener {
//                            Utils.toast(requireContext(), it.message.toString())
//                        }
//                }
//            else {
//                    Utils.dismissLoading()
//                    Utils.toast(requireContext(), "Pilih gambar  terlebih dahulu")
//                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 71 && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
//            bs.imageFormula.setImageURI(filePath)
        }
    }

    private fun initView() {
        b.rvFormula.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        b.rvFormula.layoutManager = linearLayoutManager

        reffFormula = FirebaseDatabase.getInstance().getReference("formula")

        options = FirebaseRecyclerOptions.Builder<Formula>()
            .setQuery(reffFormula, Formula::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<Formula, FirebaseViewHolder>(options) {
            override fun onBindViewHolder(
                holder: FirebaseViewHolder,
                i: Int,
                formula: Formula
            ) {
//                holder.imgFormula?.let { Glide.with(requireContext()).load(formula.image).into(it) }
                holder.tvNameFormula!!.text = formula.name
                holder.tvValuePh!!.text = Utils.formatComma2(formula.ph!!)
                holder.tvValuePpm!!.text = formula.ppm.toString()
                holder.imgMore!!.setOnClickListener {
                    val popupMenu = PopupMenu(context, holder.imgMore)
                    popupMenu.inflate(R.menu.menu_setting)
                    popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                        when (menuItem.itemId) {
                            R.id.active -> {
                                setActive(formula.id, formula.name, formula.ph!!, formula.ppm!!)
                                return@setOnMenuItemClickListener true
                            }

                            R.id.edit -> {
                                return@setOnMenuItemClickListener true
                            }

                            R.id.delete -> {
                                delFormula(formula.name, formula.id)
                                return@setOnMenuItemClickListener true
                            }
                        }
                        false
                    }
                    popupMenu.show()
                }
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): FirebaseViewHolder {
                return FirebaseViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.list_formula, parent, false)
                )
            }
        }
        adapter.notifyDataSetChanged()
        b.rvFormula.adapter = adapter
    }

    private fun delFormula(name: String, id: String) {
        AlertDialog.Builder(requireContext())
            .setMessage("Anda yakin ingin menghapus formula $name?")
            .setPositiveButton(
                "Ya"
            ) { _: DialogInterface?, _: Int ->
                FirebaseDatabase.getInstance().getReference("formula").child(id).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Formula $name berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .setNegativeButton(
                "Tidak"
            ) { dialog: DialogInterface?, _: Int -> dialog!!.dismiss() }
            .show()
    }

    private fun setActive(id: String, name: String, ph: Float, ppm: Int) {
        val update: MutableMap<String, Any> = HashMap()
        update["idCal"] = id
        update["phCal"] = ph
        update["ppmCal"] = ppm
        FirebaseDatabase.getInstance().reference.child("nutrisi").updateChildren(update)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}