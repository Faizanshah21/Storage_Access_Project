package com.example.storage_access_project

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.storage_access_project.databinding.ActivityMainBinding

// This activity is for storage access in Android version 9 and below

class AndroidPAndBelowStorageAccessActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        setClickListeners()
    }

    private fun setClickListeners() {
        mViewBinding.apply {
            btnPickMedia.setOnClickListener {
                if (!checkReadPermission())
                    requestForPermissions()
                else
                    startMediaLauncher()
            }
            btnPickDocuments.setOnClickListener {

            }
            btnPickInAndroid10AndAbove.setOnClickListener {
                startActivity(
                    Intent(
                        this@AndroidPAndBelowStorageAccessActivity,
                        AndroidQAndAboveStorageAccessActivity::class.java
                    )
                )
            }
        }
    }

    private fun startMediaLauncher() {
        mediaPickerLauncher.launch(
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
        )
    }

    private var mediaPickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let {
                setViews(it)
            }
        }

    private fun setViews(uri: Uri) {
        mViewBinding.apply {
            IvImage.setImageURI(uri)
            tvName.text = getFileName(uri)
            tvMimeType.text = getMimeType(uri)
            tvSize.text = "${getFileSizeInKB(uri)} KB"
            getFileFromUri(uri)?.run {
                tvFileCreated.text = "Yes"
            } ?: run {
                tvFileCreated.text = "No"
            }
            tvBase64String.text = uri.getUriFromBase64(this@AndroidPAndBelowStorageAccessActivity)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startMediaLauncher()
            }
        }

    private fun requestForPermissions() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

}