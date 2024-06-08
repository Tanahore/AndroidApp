package com.example.tanahore.injection

import android.content.Context
import com.example.tanahore.data.retrofit.ConfigApi
import com.example.tanahore.repository.CameraRepository

object Data {
    fun provideRepository(context: Context): CameraRepository {
        val apiService = ConfigApi.getApiService()
        return CameraRepository(apiService)
    }
}