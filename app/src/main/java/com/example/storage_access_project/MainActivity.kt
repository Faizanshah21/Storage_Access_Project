package com.example.storage_access_project

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.storage_access_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // region class variables
    private lateinit var mViewBinding: ActivityMainBinding
    private var isPickingMedia = false
    // end region

    // region lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        setClickListeners()
    }
    // end region

    // region listeners
    private fun setClickListeners() {
        mViewBinding.apply {
            btnPickMedia.setOnClickListener {
                isPickingMedia = true
                isAndroid11OrAbove {
                    if(it)
                        startPhotoPickerLauncher()
                    else {
                        if (!checkReadPermission())
                            requestForPermissions()
                        else
                            startMediaLauncher()
                    }
                }
            }
            btnPickDocuments.setOnClickListener {
                isPickingMedia = false
                isAndroid13OrAbove {
                    if (it)
                        startDocumentLauncher()
                    else {
                        if (!checkReadPermission())
                            requestForPermissions()
                        else
                            startDocumentLauncher()
                    }
                }
            }
        }
    }
    // end region

    // region launchers and callbacks
    private fun startDocumentLauncher() {
        documentPickerLauncher.launch(
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "application/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
        )
    }

    private var documentPickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let {
                setViews(it)
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

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                if (isPickingMedia) startMediaLauncher() else startDocumentLauncher()
            }
        }

    private fun requestForPermissions() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val photoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                setViews(it)
            }
        }

    private fun startPhotoPickerLauncher() {
        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    // end region

    // region private methods
    private fun setViews(uri: Uri) {
        mViewBinding.apply {
            if (isPickingMedia)
                IvImage.setImageURI(uri)
            else
                IvImage.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_document))
            tvName.text = getFileName(uri)
            tvMimeType.text = getMimeType(uri)
            tvSize.text = "${getFileSizeInKB(uri)} KB"
            getFileFromUri(uri)?.run {
                tvFileCreated.text = "Yes"
            } ?: run {
                tvFileCreated.text = "No"
            }
            tvBase64String.text = uri.getUriFromBase64(this@MainActivity)
        }
    }
    // end region

}