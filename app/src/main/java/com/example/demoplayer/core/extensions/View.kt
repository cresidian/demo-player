package com.example.demoplayer.core

import android.view.View

fun View.setVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.setInvisible(isInvisible: Boolean) {
    this.visibility = if (isInvisible) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}

fun View.setEnable(isEnable: Boolean) {
    this.isEnabled = isEnable
}
