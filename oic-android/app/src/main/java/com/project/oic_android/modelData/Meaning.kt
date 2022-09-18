package com.project.oic_android.modelData

import com.google.gson.annotations.SerializedName

data class Meaning(
    @SerializedName("antonyms")
    val antonyms: List<String>,
    @SerializedName("synonyms")
    val synonyms: List<String>
)