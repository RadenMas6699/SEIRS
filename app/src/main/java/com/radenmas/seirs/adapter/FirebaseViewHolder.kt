/*
 * Created by RadenMas on 29/8/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.seirs.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.radenmas.seirs.R
import de.hdodenhof.circleimageview.CircleImageView

class FirebaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // List Formula
//    var imgFormula: CircleImageView? = null
    var tvNameFormula: TextView? = null
    var imgMore: ImageView? = null
    var tvValuePh: TextView? = null
    var tvValuePpm: TextView? = null

    init {
        // List Formula
//        imgFormula = itemView.findViewById(R.id.imgFormula)
        tvNameFormula = itemView.findViewById(R.id.tvNameFormula)
        imgMore = itemView.findViewById(R.id.imgMore)
        tvValuePh = itemView.findViewById(R.id.tvValuePh)
        tvValuePpm = itemView.findViewById(R.id.tvValuePpm)
    }
}