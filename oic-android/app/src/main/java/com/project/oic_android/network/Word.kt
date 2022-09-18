package com.project.oic_android.network

import java.io.Serializable

data class Word(
    val img: Int,
    val word_eng: String,
    val word_kor: String
    ) : Serializable