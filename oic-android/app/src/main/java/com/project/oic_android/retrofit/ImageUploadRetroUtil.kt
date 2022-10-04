package com.project.oic_android.retrofit

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET

object ImageUploadRetroUtil{
    val api : ImageUploadNetWorkAPI by lazy { apiInit() }
    private var testRetrofit : Retrofit? = null
    private const val TEST_ADDR = "http://"

    private fun apiInit() : ImageUploadNetWorkAPI {
        val testRetrofit = testRetrofit
        val retrofit = testRetrofit ?: run {
            Retrofit.Builder()
                .baseUrl("$TEST_ADDR/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .also { this.testRetrofit = it }
        }
        return retrofit.create(ImageUploadNetWorkAPI::class.java)

    }
}

//API 인터페이스
interface ImageUploadNetWorkAPI {

    @GET("test")
    fun test(@Body data : GetBody): Call<JsonObject>

}

//서버와의 통신에 사용할 파라미터 data class
data class GetBody(
    @SerializedName("word") val word : String
)