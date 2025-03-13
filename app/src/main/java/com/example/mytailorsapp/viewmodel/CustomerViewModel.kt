package com.example.mytailorsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytailorsapp.data.CustomerDao
import com.example.mytailorsapp.data.InventoryDao
import com.example.mytailorsapp.database.CustomerEntity
import com.example.mytailorsapp.database.InventoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CustomerViewModel(
    private val customerDao: CustomerDao,
    private val inventoryDao: InventoryDao
) : ViewModel() {

    // 🔹 Holds customer data as StateFlow (better for Jetpack Compose)
    private val _selectedCustomer = MutableStateFlow<CustomerEntity?>(null)
    val selectedCustomer: StateFlow<CustomerEntity?> get() = _selectedCustomer

    // 🔹 Holds inventory data as Flow
    private val _inventoryItems = MutableStateFlow<List<InventoryItem>>(emptyList())
    val inventoryItems: StateFlow<List<InventoryItem>> get() = _inventoryItems

    // 🔹 Fetch the logged-in customer ID
    fun getLoggedInCustomerId(callback: (Int?) -> Unit) {
        viewModelScope.launch {
            val customerId = customerDao.getLoggedInCustomerId()
            callback(customerId)  // 🔹 Pass customerId back to UI
        }
    }

    // 🔹 Fetch customer by ID
    fun fetchCustomerById(customerId: Int) {
        viewModelScope.launch {
            val customer = customerDao.getCustomerById(customerId)
            _selectedCustomer.value = customer  // Update UI StateFlow
        }
    }

    // 🔹 Fetch all inventory items from DB
    fun fetchInventoryItems() {
        viewModelScope.launch {
            _inventoryItems.value = inventoryDao.getAllInventoryItems()
        }
    }

    // 🔹 Update customer profile
    fun updateCustomerProfile(name: String, contact: String, email: String, address: String, password: String) {
        viewModelScope.launch {
            _selectedCustomer.value?.let {
                val updatedCustomer = it.copy(name = name, contact = contact, email = email, address = address, password = password)
                customerDao.updateCustomer(updatedCustomer)
                _selectedCustomer.value = updatedCustomer  // Notify UI
            }
        }
    }
}

// 🔹 ViewModel Factory to provide CustomerDao & InventoryDao
class CustomerViewModelFactory(
    private val customerDao: CustomerDao,
    private val inventoryDao: InventoryDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CustomerViewModel(customerDao, inventoryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
