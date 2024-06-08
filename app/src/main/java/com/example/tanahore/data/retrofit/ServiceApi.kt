package com.example.tanahore.data.retrofit

import androidx.lifecycle.LiveData
import com.example.tanahore.data.response.ArticleResponse
import com.example.tanahore.data.response.DetailArticleResponse
import com.example.tanahore.data.response.IotResponse
import com.example.tanahore.data.response.ScanResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ServiceApi {
    @GET("/mobile/api/articles/search/{title}")
    fun searchArticles(
        @Path("title") title: String,
    ): Call<ArticleResponse>

    @GET("/mobile/api/articles/{id}")
    fun getDetail(
        @Path("id") id: Int
    ): Call<DetailArticleResponse>

    @GET("/")
    fun home(
    ): Call<DetailArticleResponse>

    @Multipart
    @POST("/mobile/api/predict/soil")
    suspend fun postImage(
        @Part image: MultipartBody.Part
    ): ScanResponse

    @FormUrlEncoded
    @POST("/mobile/api/device/{id}/predict")
    suspend fun postPh(
        @Path("id")  id: String,
        @Field("jenisTanah") jenisTanah: String,
    ): IotResponse
}