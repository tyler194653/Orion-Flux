package com.example.mobile_employee_simple.services

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraService(private val context: Context) {
    
    private var currentPhotoPath: String? = null
    
    /**
     * 检查相机权限
     */
    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * 创建图片文件
     */
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir("Photos")
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
    
    /**
     * 获取相机Intent
     */
    fun getCameraIntent(): Intent? {
        val photoFile = try {
            createImageFile()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        
        photoFile?.let {
            val photoURI = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                it
            )
            
            return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }
        }
        
        return null
    }
    
    /**
     * 获取当前照片路径
     */
    fun getCurrentPhotoPath(): String? = currentPhotoPath
    
    /**
     * 清除当前照片路径
     */
    fun clearCurrentPhotoPath() {
        currentPhotoPath = null
    }
    
    /**
     * 检查存储权限
     */
    fun hasStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * 获取图库选择Intent
     */
    fun getGalleryIntent(): Intent {
        return Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }
    
    /**
     * 删除临时照片文件
     */
    fun deleteTempPhoto() {
        currentPhotoPath?.let { path ->
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
        clearCurrentPhotoPath()
    }
    
    /**
     * 获取文件URI
     */
    fun getFileUri(filePath: String): Uri? {
        val file = File(filePath)
        return if (file.exists()) {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } else {
            null
        }
    }
} 