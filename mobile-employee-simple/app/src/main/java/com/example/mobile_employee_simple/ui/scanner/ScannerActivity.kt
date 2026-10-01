package com.example.mobile_employee_simple.ui.scanner

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.data.DataRepositoryProvider
import com.example.mobile_employee_simple.data.repository.CargoInfo
import com.example.mobile_employee_simple.databinding.ActivityScannerBinding
import com.example.mobile_employee_simple.utils.LanguageManager
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.Size
import kotlinx.coroutines.launch

class ScannerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SCAN_MODE = "extra_scan_mode"
        const val EXTRA_BARCODE_FORMAT = "extra_barcode_format"

        const val MODE_STOCK_IN = "mode_stock_in"
        const val MODE_STOCK_OUT = "mode_stock_out"
        const val MODE_CAMERA_SCAN = "mode_camera_scan"

        const val RESULT_BARCODE = "SCAN_RESULT"
        const val RESULT_CARGO_INFO = "EXTRA_CARGO_INFO"
        const val RESULT_OPERATION_CONFIRMED = "EXTRA_OPERATION_CONFIRMED"

        private const val PERMISSION_REQUEST_CAMERA = 1001
    }

    private lateinit var binding: ActivityScannerBinding
    private lateinit var beepManager: BeepManager
    private val inventoryRepository = DataRepositoryProvider.inventoryRepository

    private var scanMode = MODE_CAMERA_SCAN
    private var isTorchOn = false
    private var currentCargoInfo: CargoInfo? = null
    private var lastScannedBarcode: String? = null
    private var stockInQuantity = 1
    private var stockOutQuantity = 1
    private var hasConfirmedOperation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.init(this)

        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scanMode = intent.getStringExtra(EXTRA_SCAN_MODE) ?: MODE_CAMERA_SCAN

        beepManager = BeepManager(this)
        beepManager.isBeepEnabled = true
        beepManager.isVibrateEnabled = true

        setupViewfinder()
        setupUI()
        setupListeners()

        checkCameraPermission()
    }

    private fun setupViewfinder() {
        val scannerView = binding.barcodeScannerView

        // 优化取景框：在竖屏界面顶部配置横向宽矩形取景框，方便条形码与二维码扫码
        scannerView.post {
            val viewWidth = scannerView.width
            if (viewWidth > 0) {
                val frameWidth = (viewWidth * 0.85).toInt().coerceAtLeast(320)
                val frameHeight = (frameWidth * 0.50).toInt() // 2:1 横向比例
                scannerView.barcodeView.framingRectSize = Size(frameWidth, frameHeight)
            }
        }

        // 隐藏 ZXing 内置状态文本，使用定制的 UI 提示
        scannerView.statusView.text = ""
        scannerView.viewFinder.setLaserVisibility(true)
    }

    private fun setupUI() {
        val isZh = LanguageManager.isChinese(this)

        // 根据模式设置标题与操作面板
        when (scanMode) {
            MODE_STOCK_IN -> {
                binding.tvScannerTitle.setText(R.string.scan_title_stock_in)
                binding.layoutStockInActions.visibility = View.VISIBLE
                binding.layoutStockOutActions.visibility = View.GONE
            }
            MODE_STOCK_OUT -> {
                binding.tvScannerTitle.setText(R.string.scan_title_stock_out)
                binding.layoutStockInActions.visibility = View.GONE
                binding.layoutStockOutActions.visibility = View.VISIBLE
            }
            else -> {
                // 相机功能扫描模式：不显示入库出库按钮
                binding.tvScannerTitle.setText(R.string.scan_title_camera)
                binding.layoutStockInActions.visibility = View.GONE
                binding.layoutStockOutActions.visibility = View.GONE
            }
        }

        updateQuantityViews()
        resetScanState()
    }

    private fun setupListeners() {
        // 返回按钮
        binding.btnBack.setOnClickListener {
            handleDoneAndFinish()
        }

        // 手电筒开关
        binding.btnTorch.setOnClickListener {
            toggleTorch()
        }

        // 复制信息按钮
        binding.btnCopyInfo.setOnClickListener {
            copyScannedInfoToClipboard()
        }

        // 数量增加与减少（入库）
        binding.btnStockInQtyMinus.setOnClickListener {
            if (stockInQuantity > 1) {
                stockInQuantity--
                updateQuantityViews()
            }
        }
        binding.btnStockInQtyPlus.setOnClickListener {
            stockInQuantity++
            updateQuantityViews()
        }

        // 数量增加与减少（出库）
        binding.btnStockOutQtyMinus.setOnClickListener {
            if (stockOutQuantity > 1) {
                stockOutQuantity--
                updateQuantityViews()
            }
        }
        binding.btnStockOutQtyPlus.setOnClickListener {
            stockOutQuantity++
            updateQuantityViews()
        }

        // 确认入库按钮
        binding.btnConfirmStockIn.setOnClickListener {
            confirmStockIn()
        }

        // 确认出库按钮
        binding.btnConfirmStockOut.setOnClickListener {
            confirmStockOut()
        }

        // 重新扫描按钮
        binding.btnRescan.setOnClickListener {
            resumeScanning()
        }

        // 完成按钮
        binding.btnDone.setOnClickListener {
            handleDoneAndFinish()
        }

        // 点击待扫描徽章触发测试模拟扫码（便于在无实体条码环境快速验证）
        binding.tvScanBadge.setOnClickListener {
            if (currentCargoInfo == null) {
                onBarcodeDetected("6901234567890")
            }
        }
    }

    private fun updateQuantityViews() {
        binding.tvStockInQty.text = stockInQuantity.toString()
        binding.tvStockOutQty.text = stockOutQuantity.toString()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CAMERA
            )
        } else {
            startCameraPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraPreview()
            } else {
                Toast.makeText(this, R.string.scan_camera_permission_required, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCameraPreview() {
        binding.barcodeScannerView.resume()
        binding.barcodeScannerView.decodeSingle(scanCallback)
    }

    private val scanCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            val text = result?.text
            if (!text.isNullOrBlank()) {
                beepManager.playBeepSoundAndVibrate()
                onBarcodeDetected(text)
            }
        }

        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
            // 可选：渲染特征定位点
        }
    }

    private fun onBarcodeDetected(barcode: String) {
        lastScannedBarcode = barcode
        // 扫到条码后暂停取景预览，聚焦当前货物展示
        binding.barcodeScannerView.pause()

        lifecycleScope.launch {
            val cargo = inventoryRepository.getCargoByBarcode(barcode)
            currentCargoInfo = cargo

            displayCargoInfo(cargo)

            // 激活入库/出库确认按钮
            if (scanMode == MODE_STOCK_IN) {
                binding.btnConfirmStockIn.isEnabled = true
                binding.btnConfirmStockIn.alpha = 1.0f
            } else if (scanMode == MODE_STOCK_OUT) {
                binding.btnConfirmStockOut.isEnabled = true
                binding.btnConfirmStockOut.alpha = 1.0f
            }
        }
    }

    private fun displayCargoInfo(cargo: CargoInfo) {
        val isZh = LanguageManager.isChinese(this)

        // 更新状态徽章为“已识别”
        binding.tvScanBadge.text = getString(R.string.scan_status_identified)
        binding.tvScanBadge.setBackgroundResource(R.drawable.bg_badge_completed)
        binding.tvScanBadge.setTextColor(ContextCompat.getColor(this, R.color.status_completed_text))

        // 格式化输出货物信息和条码本身
        binding.tvCargoInfo.text = cargo.getFormattedInfo(isZh)
    }

    private fun resetScanState() {
        currentCargoInfo = null
        lastScannedBarcode = null
        binding.tvScanBadge.text = getString(R.string.scan_status_waiting)
        binding.tvScanBadge.setBackgroundResource(R.drawable.bg_badge_pending)
        binding.tvScanBadge.setTextColor(ContextCompat.getColor(this, R.color.status_pending_text))
        binding.tvCargoInfo.setText(R.string.scan_placeholder_hint)

        binding.btnConfirmStockIn.isEnabled = false
        binding.btnConfirmStockIn.alpha = 0.5f
        binding.btnConfirmStockOut.isEnabled = false
        binding.btnConfirmStockOut.alpha = 0.5f
    }

    private fun resumeScanning() {
        resetScanState()
        binding.barcodeScannerView.resume()
        binding.barcodeScannerView.decodeSingle(scanCallback)
    }

    private fun toggleTorch() {
        isTorchOn = !isTorchOn
        if (isTorchOn) {
            binding.barcodeScannerView.setTorchOn()
            binding.btnTorch.setImageResource(R.drawable.ic_flash_off)
            binding.btnTorch.contentDescription = getString(R.string.scan_torch_off)
        } else {
            binding.barcodeScannerView.setTorchOff()
            binding.btnTorch.setImageResource(R.drawable.ic_flash_on)
            binding.btnTorch.contentDescription = getString(R.string.scan_torch_on)
        }
    }

    private fun copyScannedInfoToClipboard() {
        val textToCopy = currentCargoInfo?.getFormattedInfo(LanguageManager.isChinese(this))
            ?: lastScannedBarcode
            ?: binding.tvCargoInfo.text.toString()

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Scanned Cargo Info", textToCopy)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, R.string.scan_copied_to_clipboard, Toast.LENGTH_SHORT).show()
    }

    private fun confirmStockIn() {
        val cargo = currentCargoInfo ?: return
        val qty = stockInQuantity
        lifecycleScope.launch {
            val result = inventoryRepository.stockIn(cargo.sku, qty)
            result.onSuccess { updatedCargo ->
                hasConfirmedOperation = true
                currentCargoInfo = updatedCargo
                displayCargoInfo(updatedCargo)

                val name = if (LanguageManager.isChinese(this@ScannerActivity)) updatedCargo.nameZh else updatedCargo.nameEn
                val msg = getString(R.string.scan_success_stock_in_format, name, qty, updatedCargo.quantity)
                Toast.makeText(this@ScannerActivity, msg, Toast.LENGTH_LONG).show()

                binding.btnConfirmStockIn.isEnabled = false
                binding.btnConfirmStockIn.alpha = 0.5f
            }.onFailure { e ->
                Toast.makeText(this@ScannerActivity, e.message ?: "Stock in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun confirmStockOut() {
        val cargo = currentCargoInfo ?: return
        val qty = stockOutQuantity
        lifecycleScope.launch {
            val result = inventoryRepository.stockOut(cargo.sku, qty)
            result.onSuccess { updatedCargo ->
                hasConfirmedOperation = true
                currentCargoInfo = updatedCargo
                displayCargoInfo(updatedCargo)

                val name = if (LanguageManager.isChinese(this@ScannerActivity)) updatedCargo.nameZh else updatedCargo.nameEn
                val msg = getString(R.string.scan_success_stock_out_format, name, qty, updatedCargo.quantity)
                Toast.makeText(this@ScannerActivity, msg, Toast.LENGTH_LONG).show()

                binding.btnConfirmStockOut.isEnabled = false
                binding.btnConfirmStockOut.alpha = 0.5f
            }.onFailure { e ->
                val errorMsg = e.message ?: ""
                if (errorMsg.startsWith("INSUFFICIENT_STOCK:")) {
                    val remaining = errorMsg.substringAfter(":").toIntOrNull() ?: 0
                    Toast.makeText(this@ScannerActivity, getString(R.string.scan_stock_out_insufficient, remaining), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ScannerActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleDoneAndFinish() {
        val intent = Intent().apply {
            lastScannedBarcode?.let {
                putExtra(RESULT_BARCODE, it)
                putExtra(Intents.Scan.RESULT, it)
            }
            currentCargoInfo?.let {
                putExtra(RESULT_CARGO_INFO, it.getFormattedInfo(LanguageManager.isChinese(this@ScannerActivity)))
            }
            putExtra(RESULT_OPERATION_CONFIRMED, hasConfirmedOperation)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            binding.barcodeScannerView.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeScannerView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.barcodeScannerView.pause()
    }
}
