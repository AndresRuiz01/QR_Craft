package com.andresruiz.qrcraft.preview

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * Saves the given bitmap to the public "Downloads" directory.
 * This function is compatible with all Android versions, including those with Scoped Storage.
 *
 * @param context The context to use.
 * @param bitmap The bitmap image to save.
 * @param displayName The desired display name for the file (e.g., "MyQRCode.png").
 * @return The Uri of the saved file, or null if the save operation fails.
 */
fun saveBitmapToDownloads(
    context: Context,
    bitmap: Bitmap,
    displayName: String
): Uri? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        saveBitmapToDownloadsQ(context, bitmap, displayName)
    } else {
        // For older versions, you need to request the WRITE_EXTERNAL_STORAGE permission
        // This is handled separately in your Activity/Fragment
        saveBitmapToDownloadsLegacy(context, bitmap, displayName)
    }
}

/**
 * Saves a bitmap to the Downloads directory using MediaStore.
 * This is the recommended approach for Android 10 (API 29) and above.
 */
@RequiresApi(Build.VERSION_CODES.Q)
private fun saveBitmapToDownloadsQ(
    context: Context,
    bitmap: Bitmap,
    displayName: String
): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        // Specifies that the file should be placed in the Downloads sub-directory
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val resolver = context.contentResolver
    var uri: Uri? = null
    var stream: OutputStream? = null
    try {
        uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        if (uri == null) {
            throw java.io.IOException("Failed to create new MediaStore record.")
        }
        stream = resolver.openOutputStream(uri)
        if (stream == null) {
            throw java.io.IOException("Failed to get output stream.")
        }
        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
            throw java.io.IOException("Failed to save bitmap.")
        }
    } catch (e: Exception) {
        // If there's an error, delete the incomplete file entry.
        if (uri != null) {
            resolver.delete(uri, null, null)
        }
        e.printStackTrace()
        return null // Return null to indicate failure
    } finally {
        stream?.close()
    }
    return uri
}

/**
 * Saves a bitmap to the Downloads directory for devices running below Android 10.
 * Requires the WRITE_EXTERNAL_STORAGE permission.
 */
@Suppress("DEPRECATION")
private fun saveBitmapToDownloadsLegacy(
    context: Context,
    bitmap: Bitmap,
    displayName: String
): Uri? {
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val imageFile = File(downloadsDir, displayName)

    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
    } catch (e: Exception) {
        e.printStackTrace()
        return null // Return null on failure
    } finally {
        fos?.close()
    }

    // You might want to scan the file to make it appear immediately in gallery apps
    // MediaScannerConnection.scanFile(context, arrayOf(imageFile.toString()), null, null)

    return Uri.fromFile(imageFile)
}