package com.example.mobile_employee_simple.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_employee_simple.R
import com.example.mobile_employee_simple.data.model.InventoryItem
import com.example.mobile_employee_simple.databinding.FragmentGalleryBinding
import com.example.mobile_employee_simple.ui.base.BaseFragment
import com.example.mobile_employee_simple.ui.inventory.InventoryAdapter

class GalleryFragment : BaseFragment<FragmentGalleryBinding>() {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var adapter: InventoryAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGalleryBinding {
        return FragmentGalleryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryViewModel = ViewModelProvider(this)[GalleryViewModel::class.java]

        adapter = InventoryAdapter { item ->
            showProductDetailDialog(item)
        }

        binding.galleryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.galleryRecyclerView.adapter = adapter

        galleryViewModel.products.observe(viewLifecycleOwner) { items ->
            adapter.updateItems(items)
        }

        galleryViewModel.loadProducts()
    }

    override fun onResume() {
        super.onResume()
        galleryViewModel.loadProducts()
    }

    private fun showProductDetailDialog(item: InventoryItem) {
        val details = getLocalizedString(
            R.string.gallery_dialog_details_format,
            item.sku,
            item.quantity,
            item.unit,
            item.location,
            item.lastUpdated
        )
        AlertDialog.Builder(requireContext())
            .setTitle(item.name)
            .setMessage(details)
            .setPositiveButton(R.string.dialog_ok, null)
            .show()
    }
}