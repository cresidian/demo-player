package com.example.demoplayer.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.demoplayer.R
import java.io.File
import java.io.FileOutputStream

object Utils {

    fun isOnline(context: Context): Boolean {
        var isConnected = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    fun shareImage(context: Context, shareText: String = "", bitmap: Bitmap) {
        val shareImageUri = getLocalDrawableUri(context, bitmap)
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/png"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_STREAM, shareImageUri)
        }.also {
            context.startActivity(
                Intent.createChooser(
                    it,
                    context.resources.getString(R.string.share_image)
                )
            )
        }
    }

    private fun getLocalDrawableUri(context: Context, bitmap: Bitmap): Uri {
        val file = File(context.getExternalFilesDir(null), "${System.currentTimeMillis()}.png")
        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
    }

}