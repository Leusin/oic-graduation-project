package com.project.oic_android.model

import androidx.annotation.DrawableRes

data class Word(
    @DrawableRes val imageResourceId: Int,
    val word_en: String,
    val word_kr: String
    )