package com.example.tanahore.controller

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.example.tanahore.data.Results
import com.example.tanahore.data.viewmodel.LoginVM
import com.example.tanahore.preference.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Users {
    private lateinit var preferences: UserManager
    suspend fun automatedLogin(context: Context, viewModel: LoginVM, email: String, password: String): Boolean {
        preferences = UserManager(context)
        return withContext(Dispatchers.IO) {
            val response = viewModel.login(email, password).value
            withContext(Dispatchers.Main) {
                if (response != null) {
                    when (response) {
                        is Results.Loading -> {
                            // Show loading indicator if needed
                        }
                        is Results.Success -> {
                            val data = response.data
                            preferences.setToken(data.data.accessToken)
                            preferences.setTokenCreated(System.currentTimeMillis())
                            preferences.setEmail(email)
                            preferences.setPassword(password)
                            return@withContext true
                        }
                        is Results.Error -> {
                            // Toast.makeText(context, "relogin failed", Toast.LENGTH_SHORT).show()
                            return@withContext false
                        }
                    }
                }
                return@withContext false
            }
        }
    }
}