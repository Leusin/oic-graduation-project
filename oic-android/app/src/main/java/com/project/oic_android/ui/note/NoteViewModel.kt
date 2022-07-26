package com.project.oic_android.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoteViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is 단어장 Fragment"
    }
    val text: LiveData<String> = _text
}