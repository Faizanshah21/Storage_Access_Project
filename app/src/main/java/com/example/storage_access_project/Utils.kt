package com.example.storage_access_project

import android.os.Build

fun checkAndroidVersion9(response: (Boolean) -> Unit) =
    response(Build.VERSION.SDK_INT > Build.VERSION_CODES.P)