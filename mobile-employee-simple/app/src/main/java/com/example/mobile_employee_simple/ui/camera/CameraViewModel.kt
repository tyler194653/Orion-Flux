package com.example.mobile_employee_simple.ui.camera

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {
    
    private val _photoPath = MutableLiveData<String?>()
    val photoPath: LiveData<String?> = _photoPath
    
    private val _photoUri = MutableLiveData<Uri?>()
    val photoUri: LiveData<Uri?> = _photoUri
    
    private val _scanResult = MutableLiveData<String?>()
    val scanResult: LiveData<String?> = _scanResult
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun setPhotoPath(path: String) {
        _photoPath.value = path
        _photoUri.value = null // 清除URI
    }
    
    fun setPhotoUri(uri: Uri) {
        _photoUri.value = uri
        _photoPath.value = null // 清除路径
    }
    
    fun setScanResult(result: String) {
        _scanResult.value = result
    }
    
    fun clearPhoto() {
        _photoPath.value = null
        _photoUri.value = null
        _scanResult.value = null
    }
    
    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
    
    fun hasPhoto(): Boolean {
        return _photoPath.value != null || _photoUri.value != null
    }
    
    fun getCurrentPhotoPath(): String? = _photoPath.value
    
    fun getCurrentPhotoUri(): Uri? = _photoUri.value
    
    fun getCurrentScanResult(): String? = _scanResult.value
} 