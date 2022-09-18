package com.project.oic_android

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.project.oic_android.databinding.ActivityDetailWordBinding
import com.project.oic_android.modelData.Word

class WordDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailWordBinding
    private lateinit var datas: Word

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { finish() }

        GetWordData()
    }

    private fun GetWordData() {
        datas = intent.getSerializableExtra("data") as Word

        Glide.with(this).load(datas.img).into(binding.imageView)
        binding.wordEn.text = datas.word_eng
        binding.wordKr.text = datas.word_kor

        binding.audio.setOnClickListener {
            val media = MediaPlayer()
            media.setAudioAttributes(AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
            try {
                media.setDataSource(datas.audio)
                media.prepare()
                media.start()
            }catch (e:Exception){ }
        }

        binding.example.text = "예제 : " + datas.example
        binding.syno.text = "동의어 : " + datas.syno.toString()
        binding.anto.text = "반의어 : " + datas.anto.toString()
    }
}