package com.project.oic_android.modelData

import java.io.Serializable

data class Word(
    val img: Int,
    val word_eng: String,
    val word_kor: String,
    val example: String,
    val audio: String,
    val syno: List<String>,
    val anto: List<String>
    ) : Serializable