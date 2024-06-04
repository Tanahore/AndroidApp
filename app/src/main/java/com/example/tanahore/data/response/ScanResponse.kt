package com.example.tanahore.data.response

import com.google.gson.annotations.SerializedName

data class ScanResponse(

    @field:SerializedName("data")
    val data: DataFoto,

    @field:SerializedName("code")
    val code: String,

    @field:SerializedName("message")
    val message: String,
)

data class DataFoto(

    @field:SerializedName("predictedSoil")
    val predictedsoil: String,

    @field:SerializedName("plantRecommendations")
    val plantrecommendations: List<String>
)