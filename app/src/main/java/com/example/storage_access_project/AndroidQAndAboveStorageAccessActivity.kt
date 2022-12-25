package com.example.storage_access_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storage_access_project.databinding.ActivityAndroidQandAboveStorageAccessBinding

class AndroidQAndAboveStorageAccessActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivityAndroidQandAboveStorageAccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityAndroidQandAboveStorageAccessBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
    }
}