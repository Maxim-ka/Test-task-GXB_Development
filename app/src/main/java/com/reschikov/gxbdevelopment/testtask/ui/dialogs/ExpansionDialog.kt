package com.reschikov.gxbdevelopment.testtask.ui.dialogs

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.reschikov.gxbdevelopment.testtask.R

fun Activity.showAlertDialog(title: String, message: String){
    AlertDialog.Builder(this)
        .setTitle(title)
        .setIcon(R.drawable.ic_warning)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(getString(R.string.but_ok)){ dialog, which -> dialog.dismiss()}
        .create()
        .show()
}