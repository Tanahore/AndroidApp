package com.example.tanahore.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tanahore.R
import com.example.tanahore.databinding.ActivityMainBinding
import com.example.tanahore.preference.UserManager
import java.util.Date

class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: UserManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = UserManager(this)
        if (preferences.getToken() == null) {
            binding.logout.visibility = View.GONE
        }else{
            binding.logout.visibility = View.VISIBLE
        }

        setAction()
    }

    private fun setAction(){
        binding.fabCamera.setOnClickListener {
            if (preferences.getToken() == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else {
                startActivity(Intent(this, SelectActivity::class.java))
            }
        }
        binding.logout.setOnClickListener {
            dialogLogout()
        }
    }

    private fun dialogLogout() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.logout)
            setMessage(R.string.logout2)
            setCancelable(false)
            setPositiveButton("YES") { _, _ ->
                preferences.setToken(null)
                startActivity(Intent(this@MainActivity, MainActivity::class.java))
                binding.logout.visibility = View.GONE
                finish()
            }
            setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}