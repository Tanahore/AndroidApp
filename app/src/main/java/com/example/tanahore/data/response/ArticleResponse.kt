package com.example.tanahore.data.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String
)
data class DataItem(

    @field:SerializedName("imageURL")
    val image: String,

    @field:SerializedName("title")
    val name: String,

    @field:SerializedName("articleID")
    val id: Int
)