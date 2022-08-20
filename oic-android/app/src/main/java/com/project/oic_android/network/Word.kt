package com.project.oic_android.network

import androidx.annotation.DrawableRes

data class Word(
    @DrawableRes val img: Int,
    val word_eng: String,
    val word_kor: String
    )