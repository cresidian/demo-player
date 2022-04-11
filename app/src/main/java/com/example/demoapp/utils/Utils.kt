package com.example.demoapp.utils

import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Environment
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.FileProvider
import coil.load
import coil.request.CachePolicy
import com.example.demoapp.R
import com.example.demoapp.core.extensions.getBitmap
import com.example.demoapp.data.models.Image
import com.example.demoapp.presentation.views.TouchImageView
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

    fun showFullScreenImageDialog(context: Context, image: Image) {
        val dialog = Dialog(context)
        dialog.setCancelable(true)
        val window = dialog.window!!.apply {
            setContentView(layoutInflater.inflate(R.layout.dialog_full_screen_image, null))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawableResource(R.color.colorBlack_50)
        }
        val ivFullImage = window.findViewById<TouchImageView>(R.id.ivFullImage)
        val ibBack = window.findViewById<ImageButton>(R.id.ibBack)
        val ibDownload = window.findViewById<ImageButton>(R.id.ibDownload)
        val ibShare = window.findViewById<ImageButton>(R.id.ibShare)
        ivFullImage.load(image.download_url) {
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.ENABLED)
        }
        ibBack.setOnClickListener {
            dialog.cancel()
        }
        ibDownload.setOnClickListener {
            downloadImage(context, image)
            Toast.makeText(
                context,
                context.getString(R.string.downloading_image),
                Toast.LENGTH_SHORT
            ).show()
        }
        ibShare.setOnClickListener {
            shareImage(context, bitmap = ivFullImage.getBitmap())
        }
        dialog.show()
    }

    fun downloadImage(context: Context, image: Image) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri: Uri = Uri.parse(image.download_url)
        val request = DownloadManager.Request(uri)
        val fileExtension = ".jpg"
        request.setTitle(image.id)
        request.setDescription(context.getString(R.string.downloading_image))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${image.id}$fileExtension"
        )
        downloadManager.enqueue(request)
    }

}