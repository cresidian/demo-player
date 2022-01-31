package com.example.demoplayer.base

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun showToast(msg: String, toastLength: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, toastLength).show()
    }


}