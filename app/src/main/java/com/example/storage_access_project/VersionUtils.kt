package com.example.storage_access_project

import android.os.Build

inline fun <T> isAndroid9AndBelow(isAndroid9AndBelow: () -> T): T? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        isAndroid9AndBelow()
    } else null
}

inline fun <T> isAndroid10OrBelow(isAndroid9AndBelow: () -> T): T? {
    return if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        isAndroid9AndBelow()
    } else null
}

inline fun <T> isAndroid10OrAbove(isAndroid9AndBelow: () -> T): T? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        isAndroid9AndBelow()
    } else null
}

inline fun <T> isAndroid11AndAbove(isAndroid9AndBelow: () -> T): T? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        isAndroid9AndBelow()
    } else null
}

inline fun <T> isAndroid13AndAbove(isAndroid9AndBelow: () -> T): T? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        isAndroid9AndBelow()
    } else null
}