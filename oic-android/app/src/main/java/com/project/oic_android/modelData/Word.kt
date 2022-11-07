package com.project.oic_android.modelData

import java.io.Serializable
import com.google.firebase.database.Exclude

//data class Word(
//    val word_eng: String,
//    val word_kor: String,
//    val example: String,
//    val audio: String,
//    val syno: List<String>,
//    val anto: List<String>
//    ) : Serializable

data class Word(
    var word_eng: String,
    var word_kor: String
) {

    @Exclude
    fun toMap(): HashMap<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["word_eng"] = word_eng
        result["word_kor"] = word_kor

        return result
    }

}