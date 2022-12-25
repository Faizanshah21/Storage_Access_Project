package com.example.storage_access_project

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.util.*

/*region file utils*/
fun Context.getFileFromUri(uri: Uri): File? {
    return try {
        with(contentResolver) {
            val data = readUriBytes(uri) ?: return@with null
            val extension = getFileExtension(uri)
            File(
                cacheDir.path,
                "${UUID.randomUUID()}.$extension"
            ).also { file -> file.writeBytes(data) }
        }
    } catch (e: Exception) {
        print(e)
        null
    }
}

fun ContentResolver.readUriBytes(uri: Uri) = openInputStream(uri)
    ?.buffered()?.use { it.readBytes() }

fun Context.getMimeType(uri: Uri): String? {
    return try {
        contentResolver.getType(uri)
    } catch (e: Exception) {
        print(e)
        null
    }
}

fun Context.getFileExtension(uri: Uri): String? {
    return try {
        Uri.fromFile(File(uri.path))?.lastPathSegment?.split(".")?.get(1)
    } catch (e: Exception) {
        contentResolver.getType(uri)?.substringAfter("/")
    }
}

fun Context.getFileName(uri: Uri): String {
    try {
        contentResolver.query(uri, null, null, null, null)?.use {
            it.moveToNext()
            val nameColumn = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            return it.getString(nameColumn)
        }
    } catch (e: Exception) {
        print(e)
        return ""
    }
    return ""
}

fun Context.getFileSizeInKB(uri: Uri): Float {
    try {
        contentResolver.query(uri, null, null, null, null)?.use {
            it.moveToNext()
            val sizeColumn = it.getColumnIndex(OpenableColumns.SIZE)
            return it.getFloat(sizeColumn) / 1024
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return -1f
    }
    return -1f
}
/*end utils*/

/*Uri related utils*/
fun Uri.getUriFromBase64(context: Context): String? {
    try {
        return "data:${context.getMimeType(this)};base64," + convertUriToBase64(context)
    } catch (e: Exception) {
        print(e)
    }
    return null
}

private fun Uri.convertUriToBase64(context: Context): String? {
    return try {
        val fileInputStream = context.contentResolver?.openInputStream(this)
        val bytes = fileInputStream?.let { getBytes(it) }
        Base64.encodeToString(bytes, Base64.DEFAULT)
    } catch (e: Exception) {
        print(e)
        null
    }
}

private fun getBytes(inputStream: InputStream): ByteArray? {
    try {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    } catch (e: Exception) {
        print(e)
    }
    return null
}
/*end region*/