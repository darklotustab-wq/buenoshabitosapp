package com.healthtracker.app.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PhotoUtils {

    fun createMealPhotoUri(context: Context): Pair<Uri, String> {
        val dir = File(context.getExternalFilesDir(null), "meal_photos").apply { mkdirs() }
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val file = File(dir, "meal_$stamp.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        return uri to file.absolutePath
    }
}
