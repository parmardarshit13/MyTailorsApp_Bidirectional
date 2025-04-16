package com.example.mytailorsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytailorsapp.data.models.InventoryItem
import com.example.mytailorsapp.data.repository.InventoryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class InventoryViewModel(private val inventoryRepository: InventoryRepository) : ViewModel() {

    private val _inventoryItems = MutableStateFlow<List<InventoryItem>>(emptyList())
    val inventoryItems: StateFlow<List<InventoryItem>> = _inventoryItems.asStateFlow()

    fun fetchAdminInventory() {
        viewModelScope.launch {
            try {
                val items = inventoryRepository.getAllAdminInventory()
                _inventoryItems.value = items
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchCustomerInventory(customerId: Int) {
        viewModelScope.launch {
            try {
                val items = inventoryRepository.getCustomerInventory(customerId)
                _inventoryItems.value = items
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addInventoryItem(item: InventoryItem, isAdmin: Boolean) {
        viewModelScope.launch {
            inventoryRepository.insertInventoryItem(item, isAdmin)
            if (isAdmin) fetchAdminInventory() else fetchCustomerInventory(item.customerId ?: 0)
        }
    }

    fun updateInventoryItem(item: InventoryItem, isAdmin: Boolean) {
        viewModelScope.launch {
            inventoryRepository.updateInventoryItem(item, isAdmin)
            if (isAdmin) fetchAdminInventory() else fetchCustomerInventory(item.customerId ?: 0)
        }
    }

    fun deleteInventoryItem(item: InventoryItem, isAdmin: Boolean) {
        viewModelScope.launch {
            inventoryRepository.deleteInventoryItem(item, isAdmin)
            if (isAdmin) fetchAdminInventory() else fetchCustomerInventory(item.customerId ?: 0)
        }
    }

    suspend fun getInventoryItemByName(name: String, isAdmin: Boolean): InventoryItem? {
        return inventoryRepository.getInventoryItemByName(name, isAdmin)
    }
}

// ✅ Factory to create InventoryViewModel instances
class InventoryViewModelFactory(private val inventoryRepository: InventoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(inventoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
