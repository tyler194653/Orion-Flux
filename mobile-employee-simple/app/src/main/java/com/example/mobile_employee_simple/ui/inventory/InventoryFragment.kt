package com.example.mobile_employee_simple.ui.inventory

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.databinding.FragmentInventoryBinding
import com.example.mobile_employee_simple.services.CameraService
import com.example.mobile_employee_simple.services.LocationService
import com.example.mobile_employee_simple.services.NotificationService
import com.example.mobile_employee_simple.ui.auth.LoginViewModel
import com.example.mobile_employee_simple.ui.auth.LoginFragment
import com.google.zxing.integration.android.IntentIntegrator

class InventoryFragment : Fragment() {

    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var inventoryViewModel: InventoryViewModel
    private lateinit var adapter: InventoryAdapter
    private lateinit var cameraService: CameraService
    private lateinit var locationService: LocationService
    private lateinit var notificationService: NotificationService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 检查登录状态
        if (!LoginViewModel.isLoggedIn(requireContext())) {
            showLoginRequired()
            return
        }

        inventoryViewModel = ViewModelProvider(this)[InventoryViewModel::class.java]
        cameraService = CameraService(requireContext())
        locationService = LocationService(requireContext())
        notificationService = NotificationService(requireContext())

        setupUI()
        setupRecyclerView()
        observeViewModel()
        loadInventoryData()
    }
    
    private fun showLoginRequired() {
        Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
        val loginFragment = LoginFragment()
        parentFragmentManager.commit {
            replace(R.id.nav_host_fragment_content_main, loginFragment)
            // 不添加到返回栈，这样用户按返回键时不会回到库存页面
        }
    }
    
    private fun setupUI() {
        binding.apply {
            inventoryTitle.text = "库存管理"
            
            // 返回按钮
            backButton.setOnClickListener {
                goBack()
            }
            
            // 扫码入库按钮
            btnScanIn.setOnClickListener {
                startScanForStockIn()
            }
            
            // 扫码出库按钮
            btnScanOut.setOnClickListener {
                startScanForStockOut()
            }
            
            // 库存盘点按钮
            btnInventoryCheck.setOnClickListener {
                startInventoryCheck()
            }
            
            // 货位管理按钮
            btnLocationManage.setOnClickListener {
                showLocationManagement()
            }
            
            // 刷新按钮
            btnRefresh.setOnClickListener {
                refreshInventoryData()
            }
            
            // 搜索功能
            btnSearch.setOnClickListener {
                showSearchDialog()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = InventoryAdapter { item ->
            showInventoryDetail(item)
        }
        binding.inventoryRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.inventoryRecyclerView.adapter = adapter
    }
    
    private fun observeViewModel() {
        inventoryViewModel.inventoryItems.observe(viewLifecycleOwner) { items ->
            adapter.updateItems(items)
        }
        
        inventoryViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        inventoryViewModel.scanResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                handleScanResult(result)
            }
        }
        
        inventoryViewModel.operationResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is InventoryOperationResult.Success -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    refreshInventoryData()
                }
                is InventoryOperationResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun loadInventoryData() {
        inventoryViewModel.loadInventoryData()
    }
    
    private fun refreshInventoryData() {
        inventoryViewModel.refreshInventoryData()
    }
    
    private fun startScanForStockIn() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.apply {
            setDesiredBarcodeFormats(
                IntentIntegrator.CODE_128,
                IntentIntegrator.CODE_39,
                IntentIntegrator.EAN_13,
                IntentIntegrator.EAN_8,
                IntentIntegrator.QR_CODE
            )
            setPrompt("扫描产品条码进行入库")
            setCameraId(0)
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
            initiateScan()
        }
    }
    
    private fun startScanForStockOut() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.apply {
            setDesiredBarcodeFormats(
                IntentIntegrator.CODE_128,
                IntentIntegrator.CODE_39,
                IntentIntegrator.EAN_13,
                IntentIntegrator.EAN_8,
                IntentIntegrator.QR_CODE
            )
            setPrompt("扫描产品条码进行出库")
            setCameraId(0)
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
            initiateScan()
        }
    }
    
    private fun startInventoryCheck() {
        // TODO: 启动库存盘点流程
        Toast.makeText(context, "库存盘点功能开发中", Toast.LENGTH_SHORT).show()
    }
    
    private fun showLocationManagement() {
        // TODO: 显示货位管理界面
        Toast.makeText(context, "货位管理功能开发中", Toast.LENGTH_SHORT).show()
    }
    
    private fun showSearchDialog() {
        // TODO: 显示搜索对话框
        Toast.makeText(context, "搜索功能开发中", Toast.LENGTH_SHORT).show()
    }
    
    private fun showInventoryDetail(item: InventoryItem) {
        // TODO: 显示库存详情
        Toast.makeText(context, "查看详情: ${item.name}", Toast.LENGTH_SHORT).show()
    }
    
    private fun handleScanResult(result: String) {
        // 解析扫描结果
        val scanData = parseScanResult(result)
        
        // 根据当前操作类型处理扫描结果
        when (currentOperation) {
            OperationType.STOCK_IN -> {
                inventoryViewModel.processStockIn(scanData)
            }
            OperationType.STOCK_OUT -> {
                inventoryViewModel.processStockOut(scanData)
            }
            else -> {
                Toast.makeText(context, "扫描结果: $result", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun parseScanResult(result: String): ScanData {
        // TODO: 解析扫描结果，提取产品信息
        return ScanData(
            productId = result,
            productName = "产品$result",
            quantity = 1
        )
    }
    
    private fun goBack() {
        parentFragmentManager.popBackStack()
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, "扫描取消", Toast.LENGTH_SHORT).show()
            } else {
                inventoryViewModel.setScanResult(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private enum class OperationType {
        NONE, STOCK_IN, STOCK_OUT
    }
    
    private var currentOperation = OperationType.NONE
    
    data class ScanData(
        val productId: String,
        val productName: String,
        val quantity: Int
    )
}

data class InventoryItem(
    val id: String,
    val name: String,
    val sku: String,
    val quantity: Int,
    val unit: String,
    val location: String = "",
    val lastUpdated: String = ""
)

sealed class InventoryOperationResult {
    data class Success(val message: String) : InventoryOperationResult()
    data class Error(val message: String) : InventoryOperationResult()
} 