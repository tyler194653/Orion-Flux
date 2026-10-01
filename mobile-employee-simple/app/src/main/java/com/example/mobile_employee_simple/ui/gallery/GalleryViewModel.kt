package com.example.mobile_employee_simple.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_employee_simple.data.DataRepositoryProvider
import com.example.mobile_employee_simple.data.model.InventoryItem
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {

    private val inventoryRepository = DataRepositoryProvider.inventoryRepository
    private val _products = MutableLiveData<List<InventoryItem>>()
    val products: LiveData<List<InventoryItem>> = _products

    fun loadProducts() {
        viewModelScope.launch {
            _products.value = inventoryRepository.getInventoryItems()
        }
    }
}