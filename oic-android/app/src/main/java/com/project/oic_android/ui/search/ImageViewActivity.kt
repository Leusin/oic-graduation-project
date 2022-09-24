package com.project.oic_android.ui.search

import android.Manifest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.project.oic_android.R
import com.project.oic_android.databinding.ActivityImageViewBinding

class ImageViewActivity : AppCompatActivity() {

    companion object { private const val TAG = "ImageViewActivity" }

    private lateinit var binding: ActivityImageViewBinding
    private var srcURL: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
        getImageURL()

    }

    private fun setListener() {
        binding.backIcon.setOnClickListener { finish() }
    }

    private fun getImageURL() {
        srcURL = intent.getParcelableExtra("uri")

        Log.d(TAG, "이미지 uri 전달 성공")

        binding.imageViewPreview.setImageURI(srcURL)
    }
}