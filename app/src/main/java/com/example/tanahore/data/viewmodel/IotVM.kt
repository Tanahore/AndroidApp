package com.example.tanahore.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tanahore.repository.CameraRepository

class IotVM (
    private val repository: CameraRepository
    ) : ViewModel() {
    fun postPh(
        jenisTanah: String,
        id: String
    ) = repository.postPh(jenisTanah, id)
}