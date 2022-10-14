package com.radenmas.seirs.utils

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.radenmas.seirs.R

/**
 * Created by RadenMas on 30/09/2022.
 */
object Utils {
    fun formatComma0(value: Float): String {
        return "%.0f".format(value)
    }

    fun formatComma1(value: Float): String {
        return "%.1f".format(value)
    }

    fun formatComma2(value: Float): String {
        return "%.2f".format(value)
    }

    private lateinit var progress: Dialog

    fun showLoading(context: Context) {
        progress = Dialog(context)
        progress.setContentView(R.layout.dialog_progress)
        progress.window!!.setBackgroundDrawableResource(R.drawable.bg_progress)
        progress.show()
    }

    fun dismissLoading() {
        progress.dismiss()
    }

    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun snack(view: View, msg: String) {
        Snackbar.make(
            view,
            msg,
            Snackbar.LENGTH_LONG
        ).show()
    }
}