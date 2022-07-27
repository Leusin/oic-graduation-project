package com.project.oic_android.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.oic_android.R
import com.project.oic_android.model.Word

class NoteViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is 단어장 Fragment"
    }
    val text: LiveData<String> = _text

    val words : List<Word> = listOf(
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