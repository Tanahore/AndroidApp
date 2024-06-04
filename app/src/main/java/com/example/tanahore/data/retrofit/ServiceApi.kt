package com.example.tanahore.data.retrofit

import com.example.tanahore.data.response.ArticleResponse
import com.example.tanahore.data.response.DetailArticleResponse
import retrofit2.Call
import retrofit2.http.GET
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
}