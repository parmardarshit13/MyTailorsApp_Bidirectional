package com.example.mytailorsapp.viewmodel

import androidx.lifecycle.*
import com.example.mytailorsapp.data.models.InventoryItem
import com.example.mytailorsapp.data.repository.InventoryRepository
import kotlinx.coroutines.launch

class InventoryViewModel(private val inventoryRepository: InventoryRepository) : ViewModel() {

    private val _inventoryItems = MutableLiveData<List<InventoryItem>>()
    val inventoryItems: LiveData<List<InventoryItem>> = _inventoryItems

    // ✅ Fetch all admin-side inventory
    fun fetchAdminInventory() {
        viewModelScope.launch {
            try {
                val items = inventoryRepository.getAllAdminInventory()
                _inventoryItems.postValue(items)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Add item
    fun addInventoryItem(item: InventoryItem) {
        viewModelScope.launch {
            inventoryRepository.insertInventoryItem(item)
            fetchAdminInventory()
        }
    }

    // ✅ Update item
    fun updateInventoryItem(item: InventoryItem) {
        viewModelScope.launch {
            inventoryRepository.updateInventoryItem(item)
            fetchAdminInventory()
        }
    }

    // ✅ Delete item
    fun deleteInventoryItem(item: InventoryItem) {
        viewModelScope.launch {
            inventoryRepository.deleteInventoryItem(item)
            fetchAdminInventory()
        }
    }

    // ✅ Search by customer name
    fun searchByCustomerName(name: String) {
        viewModelScope.launch {
            try {
                val results = inventoryRepository.getInventoryItemByCustomerName(name)
                _inventoryItems.postValue(results)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Filter by worker name
    fun filterByWorkerName(worker: String) {
        viewModelScope.launch {
            try {
                val results = inventoryRepository.getInventoryByWorker(worker)
                _inventoryItems.postValue(results)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
class InventoryViewModelFactory(
    private val inventoryRepository: InventoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(inventoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
