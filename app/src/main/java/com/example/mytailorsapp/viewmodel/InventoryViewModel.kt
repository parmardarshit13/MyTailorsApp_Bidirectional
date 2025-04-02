package com.example.mytailorsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytailorsapp.data.InventoryDao
import com.example.mytailorsapp.database.InventoryItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class InventoryViewModel(private val inventoryDao: InventoryDao) : ViewModel() {

    // ✅ Live list of all inventory items
    private val _inventoryItems = MutableStateFlow<List<InventoryItem>>(emptyList())
    val inventoryItems: StateFlow<List<InventoryItem>> = _inventoryItems.asStateFlow()

    init {
        fetchInventoryItems()
    }

    // ✅ Fetch inventory directly using DAO
    private fun fetchInventoryItems() {
        viewModelScope.launch {
            inventoryDao.getAllInventoryItems()
                .catch { e -> e.printStackTrace() }  // ✅ Handle errors
                .collect { items ->
                    _inventoryItems.value = items
                }
        }
    }

    // ✅ Insert new inventory item
    fun addInventoryItem(item: InventoryItem) {
        viewModelScope.launch {
            inventoryDao.insertInventoryItem(item)
            fetchInventoryItems()  // Refresh list
        }
    }

    // ✅ Update existing inventory item
    fun updateInventoryItem(updatedItem: InventoryItem) {
        viewModelScope.launch {
            inventoryDao.updateInventoryItem(updatedItem)
            fetchInventoryItems()
        }
    }

    // ✅ Delete an inventory item
    fun deleteInventoryItem(item: InventoryItem) {
        viewModelScope.launch {
            inventoryDao.deleteInventoryItem(item)
            fetchInventoryItems()
        }
    }

    // ✅ Get item by ID as Flow
//    fun getInventoryItemById(id: Int): Flow<InventoryItem?> {
//        return inventoryDao.getInventoryItemById(id)
//    }

    // ✅ Get item by name as Flow
    fun getInventoryItemByName(name: String): Flow<InventoryItem?> {
        return inventoryDao.getInventoryItemByName(name)
    }
}

// ✅ Factory to create InventoryViewModel instances
class InventoryViewModelFactory(private val inventoryDao: InventoryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(inventoryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
