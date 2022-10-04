package com.project.oic_android.ui.search

import android.Manifest
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64.NO_WRAP
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.project.oic_android.R
import com.project.oic_android.databinding.ActivityImageViewBinding
import okhttp3.MediaType
import java.io.ByteArrayOutputStream
import java.io.InputStream

class ImageViewActivity : AppCompatActivity() {

    companion object { private const val TAG = "ImageViewActivity" }

    private lateinit var binding: ActivityImageViewBinding
    private var currentImageURL: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
        getImageURL()

    }

    private fun setListener() {
        binding.backIcon.setOnClickListener { finish() }
        binding.imageSearchButton.setOnClickListener{ uploadImgToServer() }
    }

    private fun getImageURL() {
        currentImageURL = intent.getParcelableExtra("uri")
        binding.imageViewPreview.setImageURI(currentImageURL)
    }

    private fun uploadImgToServer() { } // 서버로 사진 업로드
}