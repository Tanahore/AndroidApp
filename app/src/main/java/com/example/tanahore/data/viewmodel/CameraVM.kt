package com.example.tanahore.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tanahore.repository.CameraRepository
import okhttp3.MultipartBody

class CameraVM (
    private val repository: CameraRepository
) : ViewModel() {

    fun postImage(
        file: MultipartBody.Part
    ) = repository.postImage(file)
}