package com.example.demoplayer.core.extensions

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView

fun ImageView.getBitmap(): Bitmap = (this.drawable as BitmapDrawable).bitmap