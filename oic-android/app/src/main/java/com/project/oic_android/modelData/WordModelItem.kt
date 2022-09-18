package com.project.oic_android.modelData

import com.google.gson.annotations.SerializedName

data class WordModelItem(
    val word: String,
    @SerializedName("meanings")
    val meanings: List<Meaning>?
)