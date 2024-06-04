package com.example.tanahore.data.response

import com.google.gson.annotations.SerializedName

data class IotResponse(

    @field:SerializedName("data")
    val data: DataIot,

    @field:SerializedName("code")
    val code: String,

    @field:SerializedName("message")
    val message: String,
)

data class DataIot(

    @field:SerializedName("data")
    val data: DataIot2,

    @field:SerializedName("status")
    val status: DataIot3,
)

data class DataIot2(

    @field:SerializedName("plantRecommendation")
    val plantrecommendation: String,

    @field:SerializedName("suhu")
    val suhu: Float,

    @field:SerializedName("kelembapan")
    val kelembapan: Float,

    @field:SerializedName("intensitasCahaya")
    val intensitascahaya: Int,

    @field:SerializedName("ph")
    val ph: Float
)

data class DataIot3(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("message")
    val message: String
)