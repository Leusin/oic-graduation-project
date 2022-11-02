package com.project.oic_android.ui.note

import com.project.oic_android.R
import com.project.oic_android.modelData.Word

class Datasource {

    val words : MutableList<Word> = mutableListOf<Word>(
        Word(
            R.mipmap.ic_launcher,
            "cup",
            "컵",
            "",
            "https://api.dictionaryapi.dev/media/pronunciations/en/cup-us.mp3",
            listOf(),
            listOf()
        ),
        Word(
            R.mipmap.ic_launcher,
            "orange",
            "오렌지",
            "big apple",
            "https://api.dictionaryapi.dev/media/pronunciations/en/orange-us.mp3",
            listOf(),
            listOf()
        ),
        Word(
            R.mipmap.ic_launcher,
            "banana",
            "바나나",
            "big apple",
            "https://api.dictionaryapi.dev/media/pronunciations/en/banana-us.mp3",
            listOf(),
            listOf()
        ),
        Word(
            R.mipmap.ic_launcher,
            "kiwifruit",
            "키위",
            "big apple",
            "https://api.dictionaryapi.dev/media/pronunciations/en/kiwifruit-us.mp3",
            listOf(),
            listOf()
        ),
        Word(
            R.mipmap.ic_launcher,
            "apple",
            "사과",
            "big apple",
            "https://api.dictionaryapi.dev/media/pronunciations/en/apple-us.mp3",
            listOf(),
            listOf()
        ), Word(
            R.mipmap.ic_launcher,
            "pineapple",
            "파인애플",
            "big apple",
            "https://api.dictionaryapi.dev/media/pronunciations/en/pineapple-us.mp3",
            listOf(),
            listOf()
        ),
        Word(
            R.mipmap.ic_launcher,
            "peach",
            "복숭아",
            "big apple",
            "https://api.dictionaryapi.dev/media/pronunciations/en/peach-us.mp3",
            listOf(),
            listOf()
        ),
        Word(
            R.mipmap.ic_launcher,
            "happy",
            "행복한",
            "big apple",
            "https://api.dictionaryapi.dev/media/pronunciations/en/apple-us.mp3",
            listOf(),
            listOf()
        )
    )

}
