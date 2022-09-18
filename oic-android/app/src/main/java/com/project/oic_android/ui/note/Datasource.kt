package com.project.oic_android.ui.note

import com.project.oic_android.R
import com.project.oic_android.network.Word
import java.util.Collections.addAll

class Datasource {

    val words : MutableList<Word> = mutableListOf<Word>(
        Word(
            R.mipmap.ic_launcher,
            "apple",
            "사과"
        ),
        Word(
            R.mipmap.ic_launcher,
            "orange",
            "오렌지"
        ),
        Word(
            R.mipmap.ic_launcher,
            "banana",
            "바나나"
        ),
        Word(
            R.mipmap.ic_launcher,
            "kiwifruit",
            "키위"
        ),
        Word(
            R.mipmap.ic_launcher,
            "apple",
            "사과"
        ), Word(
            R.mipmap.ic_launcher,
            "pineapple",
            "파인애플"
        ),
        Word(
            R.mipmap.ic_launcher,
            "peach",
            "복숭아"
        ),
        Word(
            R.mipmap.ic_launcher,
            "apple",
            "사과"
        )
    )

}
