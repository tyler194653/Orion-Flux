package com.example.mobile_employee_simple.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_employee_simple.databinding.FragmentGalleryBinding
import com.example.mobile_employee_simple.ui.inventory.InventoryAdapter
import com.example.mobile_employee_simple.ui.inventory.InventoryItem

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: InventoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = InventoryAdapter { item ->
            showProductDetailDialog(item)
        }

        binding.galleryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.galleryRecyclerView.adapter = adapter

        galleryViewModel.products.observe(viewLifecycleOwner) { items ->
            adapter.updateItems(items)
        }

        return root
    }

    private fun showProductDetailDialog(item: InventoryItem) {
        AlertDialog.Builder(requireContext())
            .setTitle(item.name)
            .setMessage("SKU: ${item.sku}\n" +
                    "当前库存: ${item.quantity} ${item.unit}\n" +
                    "库区货位: ${item.location}\n" +
                    "最后盘点: ${item.lastUpdated}\n\n" +
                    "状态: 正常流转\n" +
                    "规格型号: 标准工业规格")
            .setPositiveButton("确定", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}