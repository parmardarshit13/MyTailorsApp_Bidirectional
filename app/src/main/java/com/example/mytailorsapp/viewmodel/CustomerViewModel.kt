package com.example.mytailorsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytailorsapp.data.CustomerDao
import com.example.mytailorsapp.data.MaterialDao
import com.example.mytailorsapp.data.InventoryDao
import com.example.mytailorsapp.database.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.mytailorsapp.R

class CustomerViewModel(
    private val customerDao: CustomerDao,
    private val materialDao: MaterialDao,
    private val inventoryDao: InventoryDao
) : ViewModel() {

    // Customer State
    private val _selectedCustomer = MutableStateFlow<CustomerEntity?>(null)
    val selectedCustomer: StateFlow<CustomerEntity?> = _selectedCustomer.asStateFlow()

    // Inventory State (Stores completed & incomplete pairs)
    private val _inventoryItems = MutableStateFlow<List<InventoryItem>>(emptyList())
    val inventoryItems: StateFlow<List<InventoryItem>> = _inventoryItems.asStateFlow()

    // Materials State
    private val _materialItems = MutableStateFlow<List<MaterialItem>>(emptyList())
    val materialItems: StateFlow<List<MaterialItem>> = _materialItems.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            if (materialDao.getMaterialCount() == 0) {
                val defaultMaterials = listOf(
                    MaterialItem(0, "Cotton", "Soft and breathable", "₹500/meter", R.drawable.cotton_texture, "Casual"),
                    MaterialItem(0, "Silk", "Smooth and shiny", "₹1200/meter", R.drawable.silk_texture, "Party Wear"),
                    MaterialItem(0, "Denim", "Strong and durable", "₹800/meter", R.drawable.denim_texture, "Casual")
                )
                materialDao.insertAll(defaultMaterials)
            }

            // Collect the Flow and update StateFlow
            materialDao.getAllMaterials().collect { materials ->
                _materialItems.value = materials
            }
        }
    }

    fun getLoggedInCustomerId(callback: (Int?) -> Unit) {
        viewModelScope.launch {
            callback(customerDao.getLoggedInCustomerId())
        }
    }

    fun fetchCustomerById(customerId: Int) {
        viewModelScope.launch {
            _selectedCustomer.value = customerDao.getCustomerById(customerId)
        }
    }

    fun updateCustomerProfile(name: String, contact: String, email: String, address: String, password: String) {
        viewModelScope.launch {
            _selectedCustomer.value?.let { customer ->
                val updatedCustomer = customer.copy(name = name, contact = contact, email = email, address = address, password = password)
                customerDao.updateCustomer(updatedCustomer)
                _selectedCustomer.value = updatedCustomer
            }
        }
    }

    // ✅ Fetching Inventory Items for a Customer
    fun fetchInventoryItems(customerId: Int) {
        viewModelScope.launch {
            inventoryDao.getInventoryByCustomerId(customerId).collect { inventoryList ->
                _inventoryItems.value = inventoryList
            }
        }
    }

    // ✅ Login Method (Updated)
    suspend fun login(email: String, password: String): Boolean {
        return customerDao.authenticate(email, password)?.let { customer ->
            customerDao.logoutAllCustomers() // Ensure only one logged-in user
            customerDao.updateLoginStatus(customer.email, true) // Mark as logged in
            _selectedCustomer.value = customer // Update LiveData
            true
        } == true
    }

    // ✅ Logout Method
    fun logout() {
        viewModelScope.launch {
            _selectedCustomer.value?.let { customer ->
                customerDao.updateLoginStatus(customer.email, false)
                _selectedCustomer.value = null
            }
        }
    }
}

// ✅ Factory for ViewModel
class CustomerViewModelFactory(
    private val customerDao: CustomerDao,
    private val materialDao: MaterialDao,
    private val inventoryDao: InventoryDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CustomerViewModel(customerDao, materialDao, inventoryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
