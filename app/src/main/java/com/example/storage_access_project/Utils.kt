package com.example.storage_access_project

import android.os.Build

fun isAndroid9OrBelow(response: (Boolean) -> Unit) =
    response(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)

fun isAndroid10OrBelow(response: (Boolean) -> Unit) =
    response(Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)

fun isAndroid10OrAbove(response: (Boolean) -> Unit) =
    response(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)

fun isAndroid11OrAbove(response: (Boolean) -> Unit) =
    response(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)

fun isAndroid13OrAbove(response: (Boolean) -> Unit) =
    response(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)