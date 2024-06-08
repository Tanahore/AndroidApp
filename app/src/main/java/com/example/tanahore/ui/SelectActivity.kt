package com.example.tanahore.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tanahore.R
import com.example.tanahore.data.Results
import com.example.tanahore.data.viewmodel.CameraVM
import com.example.tanahore.data.viewmodel.FactoryVM
import com.example.tanahore.data.viewmodel.IotVM
import com.example.tanahore.databinding.ActivitySelectBinding
import com.example.tanahore.utils.reduceFileImage
import com.example.tanahore.utils.rotateBitmap
import com.example.tanahore.utils.showToast
import com.example.tanahore.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class SelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectBinding
    private lateinit var factory: FactoryVM
    private val viewModel: CameraVM by viewModels {
        factory
    }
    private val viewModel2: IotVM by viewModels {
        factory
    }
    private var getFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = FactoryVM.getInstance(this)
        binding.cardView.visibility = View.GONE
        binding.tvRegister.visibility = View.GONE
        binding.btnPh.visibility = View.GONE
        binding.cardView2.visibility = View.GONE

        setAction()

        if (!permissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    private fun setAction() {
        binding.back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.btnCamera.setOnClickListener {
            startCameraX()
        }
        binding.btnGaleri.setOnClickListener {
            startGallery()
        }
    }

    private fun startGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImage: Uri = result.data?.data as Uri
            val mFile = uriToFile(selectedImage, this)
            getFile = mFile
            postImage(mFile)

            binding.addImage.setImageURI(selectedImage)
            binding.btnCamera.visibility = View.GONE
            binding.btnGaleri.visibility = View.GONE
            binding.cardView.visibility = View.VISIBLE
            binding.tvRegister.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun iot(jenisTanah: String) {
        Log.d("iot", "masuk")
        viewModel2.postPh("DT11", jenisTanah).observe(this) { response ->
            when (response) {
                is Results.Success -> {
                    Log.d("iot", "success")
                    binding.cardView2.visibility = View.VISIBLE
                    binding.pH.text = resources.getString(R.string.phtanah) + " " + response.data.data.data.ph
                    binding.Rekomendasi.text = resources.getString(R.string.rekomendasi) + " " + response.data.data.data.plantRecommendation
                    binding.Suhu.text = resources.getString(R.string.suhu) + " " + response.data.data.data.suhu
                    binding.Kelembapan.text = resources.getString(R.string.kelembapan) + " " + response.data.data.data.kelembapan
                    binding.IntensitasCahaya.text = resources.getString(R.string.intensitascahaya) + " " + response.data.data.data.intensitasCahaya
                    binding.progressBar.visibility = View.GONE
                }

                is Results.Error -> {
                    Log.d("iot", "error")
                    showToast(response.error)
                }

                is Results.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun postImage(image: File) {
        Log.d("postimage", "masuk")
        val file = reduceFileImage(image)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )

        viewModel.postImage(imageMultipart).observe(this) { response ->
            when (response) {
                is Results.Success -> {
//                    val name = response.data.data.contact.name
//                    Log.d("ScanResponse", "Success: $name")
                    binding.txtJenisTanah.text = resources.getString(R.string.jenis_tanah) + " " + response.data.data.predictedsoil
                    binding.txtTanaman.text = resources.getString(R.string.tanaman) + " " + response.data.data.plantrecommendations
//                    binding.txtDesc.text = response.data.data.information.description
                    binding.progressBar.visibility = View.GONE
                    binding.btnPh.visibility = View.VISIBLE
                    binding.btnPh.setOnClickListener {
                        iot(response.data.data.predictedsoil)
                    }
                }
                is Results.Error -> {
                    showToast(response.error)
                }
                is Results.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun permissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERA_X_RESULT) {
            val mFile = result.data?.getSerializableExtra("picture") as File
            val isBackCamera = result.data?.getBooleanExtra("isBackCamera", true) as Boolean
            Log.d("isBack", "before postImage")
            postImage(mFile)
            getFile = mFile
            val resultBitmap = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.addImage.setImageBitmap(resultBitmap)
            binding.btnCamera.visibility = View.GONE
            binding.btnGaleri.visibility = View.GONE
            binding.cardView.visibility = View.VISIBLE
            binding.tvRegister.visibility = View.VISIBLE
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 100
    }
}