package com.project.oic_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.project.oic_android.databinding.ActivityWordBinding
import com.project.oic_android.network.Word

class WordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWordBinding
    private lateinit var datas: Word

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { finish() }

        datas = intent.getSerializableExtra("data") as Word

        Glide.with(this).load(datas.img).into(binding.imageView)
        binding.wordEn.text = datas.word_eng
        binding.wordKr.text = datas.word_kor
    }
}