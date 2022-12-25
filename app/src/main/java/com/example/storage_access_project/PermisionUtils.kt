package com.example.storage_access_project

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

fun Context.checkReadPermission(): Boolean {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    else
        true
}