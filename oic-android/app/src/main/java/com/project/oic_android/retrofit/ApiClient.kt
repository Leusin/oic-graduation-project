package com.project.oic_android.retrofit

import com.project.oic_android.modelData.WordModelItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService{
    @GET("/api/v2/entries/en/{word}")
    suspend fun getWord(
        @Path("word") word:String
    ): Response<List<WordModelItem>>
}