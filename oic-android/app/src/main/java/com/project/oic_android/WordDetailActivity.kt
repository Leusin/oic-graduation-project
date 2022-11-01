package com.project.oic_android

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.UserDictionary.Words.addWord
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.bumptech.glide.Glide
import com.project.oic_android.databinding.ActivityDetailWordBinding
import com.project.oic_android.modelData.Word
import com.project.oic_android.ui.note.Datasource

class WordDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailWordBinding
    private lateinit var datas: Word

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookMarkIcon("not on list") //수정 요망
        setListener()
        GetWordData()
    }

    private fun setListener() {
        binding.backIcon.setOnClickListener { finish() }
        binding.bookMarkIcon.setOnClickListener { addWord() }
    }

    private fun initBookMarkIcon(mode: String) {
        if (mode == "on list"){ binding.bookMarkIcon.setImageResource(R.drawable.ic_baseline_bookmark_24) }
        else if (mode != "on list") { binding.bookMarkIcon.setImageResource(R.drawable.ic_baseline_bookmark_border_24) }
    }

    private fun addWord() {
        initBookMarkIcon("on list")
        Toast.makeText(this, "단어장에 저장하였습니다", Toast.LENGTH_SHORT).show()
        // if (datas.word_eng in) initBookMarkIcon("on list") { initBookMarkIcon("on list") } else { initBookMarkIcon("not on list") }
    }

    private fun GetWordData() {
        datas = intent.getSerializableExtra("data") as Word

        //Glide.with(this).load(datas.img).into(binding.imageView)
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