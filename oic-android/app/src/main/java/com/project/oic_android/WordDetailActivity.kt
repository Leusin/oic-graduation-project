package com.project.oic_android

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.UserDictionary.Words.addWord
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.oic_android.databinding.ActivityDetailWordBinding
import com.project.oic_android.modelData.ResultTransferPapago
import com.project.oic_android.modelData.Word
import com.project.oic_android.retrofit.NaverAPI
import com.project.oic_android.ui.note.Datasource
import kotlinx.android.synthetic.main.activity_detail_word.*
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.list_item.word_en
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WordDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailWordBinding
    private lateinit var datas: String
    private lateinit var datasKr: String
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookMarkIcon("not on list") //수정 요망
        setListener()
        GetWordData()

        databaseRef =
            FirebaseDatabase.getInstance("https://oicproject-fda8d-default-rtdb.firebaseio.com/").reference

    }

    private fun setListener() {
        binding.backIcon.setOnClickListener { finish() }
        binding.bookMarkIcon.setOnClickListener { addWord() }
    }

    private fun initBookMarkIcon(mode: String) {
        if (mode == "on list") {
            binding.bookMarkIcon.setImageResource(R.drawable.ic_baseline_bookmark_24)
        } else if (mode != "on list") {
            binding.bookMarkIcon.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
        }
    }


    private fun addWord() {
        initBookMarkIcon("on list")
        Toast.makeText(this, "단어장에 저장하였습니다", Toast.LENGTH_SHORT).show()
        val wordEn = wordinfo_en.text.toString()
        val wordKr = wordinfo_kr.text.toString()

        saveWord(wordEn, wordKr)
        // if (datas.word_eng in) initBookMarkIcon("on list") { initBookMarkIcon("on list") } else { initBookMarkIcon("not on list") }
    }

    fun saveWord(wordEn: String, wordKr: String) {
        val key: String? = databaseRef.child("comments").push().getKey()
        val comment = Word(wordEn, wordKr)
        val commentValues: HashMap<String, Any> = comment.toMap()

        val childUpdates: MutableMap<String, Any> = HashMap()
        childUpdates["/comments/$key"] = commentValues

        databaseRef.updateChildren(childUpdates)
    }

    private fun GetWordData() {
        //datas = intent.getSerializableExtra("data") as Word
        //datas = "chair"
        //datasKr = "의자"
//        datas = intent.getStringExtra("data") as String
//        datasKr = intent.getStringExtra("dataKr") as String

        if (!TextUtils.isEmpty(intent.getStringExtra("data"))) {
            datas = intent.getStringExtra("data") as String
            datasKr = intent.getStringExtra("dataKr") as String
        }
        else if (!TextUtils.isEmpty(intent.getStringExtra("data2"))) {
            datas = intent.getStringExtra("data2") as String
            datasKr = intent.getStringExtra("dataKr2") as String
        }
        else {
            datas = intent.getStringExtra("data1") as String
            datasKr = intent.getStringExtra("dataKr1") as String
            //datas = "chair"
            //datasKr = "의자"
        }

        //Glide.with(this).load(datas.img).into(binding.imageView)
        binding.wordinfoEn.text = datas
        binding.wordinfoKr.text = datasKr
        //binding.wordinfoKr.text = datas.word_kor
        //binding.wordKr.text = datas.word_kor

        binding.audio.setOnClickListener {
            val media = MediaPlayer()
            media.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            )
            try {
                //media.setDataSource(datas.audio)
                media.prepare()
                media.start()
            } catch (e: Exception) {
            }
        }

        //binding.example.text = "예제 : " + datas.example
        //binding.syno.text = "동의어 : " + datas.syno.toString()
        //binding.anto.text = "반의어 : " + datas.anto.toString()

        //사물인식
        if(datas=="chair"){
            binding.example.text = "예제"
            binding.exampleEx.text = "The height of the bicycle seat is adjustable."
            binding.syno.text = "동의어"
            binding.synoEx.text = "stool"
        }else if(datas=="laptop"){
            binding.wordinfoKr.text = "노트북"
            binding.example.text = "예제"
            binding.exampleEx.text = "I left my laptop on the bus here."
            binding.syno.text = "동의어"
            binding.synoEx.text = "desktop computer"
        }
        else if(datas=="vase"){
            binding.example.text = "예제"
            binding.exampleEx.text = "a vase of flowers"
            binding.syno.text = ""
            binding.synoEx.text = ""
            binding.anto.text = ""
            binding.antoEx.text  = ""
            binding.wordinfoKr.text = "꽃병"
        }
        //단어검색
        if(datas=="succeed"){
            binding.example.text = "예제"
            binding.exampleEx.text = "You will succeed to get a job this time. So cheer up"
            binding.syno.text = "동의어"
            binding.synoEx.text = "prosper, make it"
            binding.anto.text = "반의어"
            binding.antoEx.text = "fail"
        }else if(datas=="graduate"){
            binding.example.text = "예제"
            binding.exampleEx.text = "Martha graduated from university two years ago."
            binding.syno.text = "동의어"
            binding.synoEx.text = "mark off, proportion"
            binding.anto.text = "반의어"
            binding.antoEx.text = "regress, be demoted"
            binding.wordinfoKr.text = "졸업하다"
        }else if(datas=="explain"){
            binding.example.text = "예제"
            binding.exampleEx.text = "I'll explain the rules of the game."
            binding.syno.text = "동의어"
            binding.synoEx.text = "describe, teach"
            binding.anto.text = ""
            binding.antoEx.text = ""
            binding.wordinfoKr.text = "설명하다"
        }

//        binding.example.text = "예제"
//        binding.syno.text = "동의어"
//        binding.anto.text = "반의어"

    }
}