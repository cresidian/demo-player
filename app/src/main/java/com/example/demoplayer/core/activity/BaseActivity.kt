package com.example.demoplayer.core.activity

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.demoplayer.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    fun showToast(msg: String, toastLength: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, toastLength).show()
    }

    fun showSnackBar(
        msg: String,
        view: View = window.decorView.rootView,
        snackBarLength: Int = Snackbar.LENGTH_SHORT
    ) {
        Snackbar.make(
            view, msg, snackBarLength
        ).show()
    }

    fun showDialog(
        title: String = "",
        message: String,
        onPositiveButtonClicked: () -> Unit = {},
        onNegativeButtonClicked: () -> Unit = {},
        positiveButtonText: Int = R.string.yes,
        negativeButtonText: Int = R.string.no,
    ) {
        val dialogBuilder = AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(
                positiveButtonText
            ) { dialog, id ->
                onPositiveButtonClicked.invoke()
                dialog.cancel()
            }
            setNegativeButton(
                negativeButtonText
            ) { dialog, id ->
                onNegativeButtonClicked.invoke()
                dialog.cancel()
            }
        }.create().show()
    }

}