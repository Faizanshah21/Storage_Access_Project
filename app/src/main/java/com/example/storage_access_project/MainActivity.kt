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
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    // region class variables
    private lateinit var mViewBinding: ActivityMainBinding
    private var isPickingMedia = false
    private val dummyImageUrl = "https://i.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI"
    // end region

    // region lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        setImageFromUrl()
        setClickListeners()
    }
    // end region

    // region listeners
    private fun setClickListeners() {
        mViewBinding.apply {
            btnPickMedia.setOnClickListener {
                isPickingMedia = true
                isAndroid13AndAbove {
                    startPhotoPickerLauncher()
                } ?: run {
                    if (checkReadPermission())
                        startMediaLauncher()
                    else
                        requestReadPermissions()
                }
            }
            btnPickDocuments.setOnClickListener {
                isPickingMedia = false
                isAndroid13AndAbove {
                    startDocumentLauncher()
                } ?: run {
                    if (checkReadPermission())
                        startDocumentLauncher()
                    else
                        requestReadPermissions()
                }
            }
            btnDownloadImage.setOnClickListener {
                isAndroid9AndBelow {
                    if (checkWritePermission()) downloadFileFromURL(dummyImageUrl)
                    else requestWritePermissions()
                } ?: downloadFileFromURL(dummyImageUrl)
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

    private val readPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                if (isPickingMedia) startMediaLauncher() else startDocumentLauncher()
            }
        }

    private fun requestReadPermissions() {
        readPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val writePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                downloadFileFromURL(dummyImageUrl)
            }
        }

    private fun requestWritePermissions() {
        writePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

    private fun setImageFromUrl() {
        Picasso.get().load(dummyImageUrl).into(mViewBinding.IvDownloadableImage)
    }
    // end region

}