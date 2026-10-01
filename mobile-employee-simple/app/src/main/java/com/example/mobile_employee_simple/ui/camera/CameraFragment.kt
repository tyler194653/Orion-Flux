package com.example.mobile_employee_simple.ui.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.databinding.FragmentCameraBinding
import com.example.mobile_employee_simple.services.CameraService
import com.example.mobile_employee_simple.ui.auth.LoginViewModel
import com.example.mobile_employee_simple.ui.auth.LoginFragment
import com.google.zxing.integration.android.IntentIntegrator

class CameraFragment : Fragment() {
    
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var cameraService: CameraService
    
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(context, getString(R.string.camera_permission_required), Toast.LENGTH_SHORT).show()
        }
    }
    
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val photoPath = cameraService.getCurrentPhotoPath()
            if (photoPath != null) {
                cameraViewModel.setPhotoPath(photoPath)
                binding.imagePreview.visibility = View.VISIBLE
                binding.imagePreview.setImageURI(Uri.parse(photoPath))
            }
        }
    }
    
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                cameraViewModel.setPhotoUri(uri)
                binding.imagePreview.visibility = View.VISIBLE
                binding.imagePreview.setImageURI(uri)
            }
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 检查登录状态
        if (!LoginViewModel.isLoggedIn(requireContext())) {
            showLoginRequired()
            return
        }
        
        cameraService = CameraService(requireContext())
        cameraViewModel = ViewModelProvider(this)[CameraViewModel::class.java]
        
        setupUI()
        observeViewModel()
    }
    
    private fun showLoginRequired() {
        Toast.makeText(context, getString(R.string.camera_login_required), Toast.LENGTH_SHORT).show()
        val loginFragment = LoginFragment()
        parentFragmentManager.commit {
            replace(R.id.nav_host_fragment_content_main, loginFragment)
            // 不添加到返回栈，这样用户按返回键时不会回到相机页面
        }
    }
    
    private fun setupUI() {
        binding.apply {
            // 拍照按钮
            btnTakePhoto.setOnClickListener {
                if (cameraService.hasCameraPermission()) {
                    openCamera()
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
            
            // 选择图片按钮
            btnSelectImage.setOnClickListener {
                if (cameraService.hasStoragePermission()) {
                    openGallery()
                } else {
                    // 请求存储权限
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        1001
                    )
                }
            }
            
            // 扫码按钮
            btnScanQr.setOnClickListener {
                startQRCodeScanner()
            }
            
            // 条码扫描按钮
            btnScanBarcode.setOnClickListener {
                startBarcodeScanner()
            }
            
            // 确认按钮
            btnConfirm.setOnClickListener {
                val photoPath = cameraViewModel.photoPath.value
                val photoUri = cameraViewModel.photoUri.value
                
                if (photoPath != null || photoUri != null) {
                    Toast.makeText(context, getString(R.string.camera_photo_saved), Toast.LENGTH_SHORT).show()
                    clearPhoto()
                } else {
                    Toast.makeText(context, getString(R.string.camera_please_take_photo), Toast.LENGTH_SHORT).show()
                }
            }
            
            // 清除按钮
            btnClear.setOnClickListener {
                clearPhoto()
            }
        }
    }
    
    private fun observeViewModel() {
        cameraViewModel.photoPath.observe(viewLifecycleOwner) { path ->
            if (path != null) {
                binding.imagePreview.visibility = View.VISIBLE
                binding.imagePreview.setImageURI(Uri.parse(path))
            }
        }
        
        cameraViewModel.photoUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                binding.imagePreview.visibility = View.VISIBLE
                binding.imagePreview.setImageURI(uri)
            }
        }
        
        cameraViewModel.scanResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                binding.tvScanResult.text = getString(R.string.camera_scan_result_format, result)
                binding.tvScanResult.visibility = View.VISIBLE
            }
        }
    }
    
    private fun openCamera() {
        val intent = cameraService.getCameraIntent()
        if (intent != null) {
            cameraLauncher.launch(intent)
        } else {
            Toast.makeText(context, getString(R.string.camera_cannot_open), Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun openGallery() {
        val intent = cameraService.getGalleryIntent()
        galleryLauncher.launch(intent)
    }
    
    private fun startQRCodeScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.apply {
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            setPrompt(getString(R.string.camera_qr_scan_prompt))
            setCameraId(0)
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
            initiateScan()
        }
    }
    
    private fun startBarcodeScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.apply {
            setDesiredBarcodeFormats(
                IntentIntegrator.CODE_128,
                IntentIntegrator.CODE_39,
                IntentIntegrator.EAN_13,
                IntentIntegrator.EAN_8,
                IntentIntegrator.UPC_A,
                IntentIntegrator.UPC_E
            )
            setPrompt(getString(R.string.camera_barcode_scan_prompt))
            setCameraId(0)
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
            initiateScan()
        }
    }
    
    private fun clearPhoto() {
        cameraViewModel.clearPhoto()
        binding.imagePreview.visibility = View.GONE
        binding.tvScanResult.visibility = View.GONE
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, getString(R.string.camera_scan_canceled), Toast.LENGTH_SHORT).show()
            } else {
                cameraViewModel.setScanResult(result.contents)
                Toast.makeText(context, getString(R.string.camera_scan_success, result.contents), Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(context, getString(R.string.camera_storage_permission_required), Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 