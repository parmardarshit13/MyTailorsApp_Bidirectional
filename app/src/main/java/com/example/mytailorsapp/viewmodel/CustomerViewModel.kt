package com.example.mytailorsapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytailorsapp.data.models.CustomerEntity
import com.example.mytailorsapp.data.models.InventoryItem
import com.example.mytailorsapp.data.models.MaterialItem
import com.example.mytailorsapp.data.repository.CustomerRepository
import com.example.mytailorsapp.data.repository.InventoryRepository
import com.example.mytailorsapp.data.repository.MaterialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomerViewModel(
    private val customerRepository: CustomerRepository,
    private val materialRepository: MaterialRepository,
    private val inventoryRepository: InventoryRepository
) : ViewModel() {

    private val _selectedCustomer = MutableStateFlow<CustomerEntity?>(null)
    val selectedCustomer: StateFlow<CustomerEntity?> = _selectedCustomer.asStateFlow()

    private val _materialItems = MutableStateFlow<List<MaterialItem>>(emptyList())
    val materialItems: StateFlow<List<MaterialItem>> = _materialItems.asStateFlow()

    private val _inventoryItems = MutableStateFlow<List<InventoryItem>>(emptyList())
    val inventoryItems: StateFlow<List<InventoryItem>> = _inventoryItems.asStateFlow()

    init {
        fetchAllMaterials()
    }

    private fun fetchAllMaterials() {
        viewModelScope.launch {
            val materials = materialRepository.getAllMaterials()
            _materialItems.value = materials
        }
    }

    fun fetchCustomerById(customerId: Int) {
        viewModelScope.launch {
            val customer = customerRepository.getCustomerById(customerId)
            _selectedCustomer.value = customer
        }
    }

    fun getLoggedInCustomerId(callback: (Int?) -> Unit) {
        viewModelScope.launch {
            val id = customerRepository.getLoggedInCustomerId()
            callback(id)
        }
    }

    fun updateCustomerProfile(customer: CustomerEntity) {
        viewModelScope.launch {
            customerRepository.updateCustomerProfile(customer)
            _selectedCustomer.value = customer
        }
    }

//    fun fetchInventoryItems(customerId: Int) {
//        viewModelScope.launch {
//            val items = inventoryRepository.getCustomerInventory(customerId)
//            _inventoryItems.value = items
//        }
//    }

    suspend fun login(email: String, password: String): Boolean {
        val customer = customerRepository.login(email, password)
        return if (customer != null) {
            _selectedCustomer.value = customer
            true
        } else false
    }

    fun logout() {
        viewModelScope.launch {
            _selectedCustomer.value?.let {
                customerRepository.logout(it.email)
                _selectedCustomer.value = null
            }
        }
    }

    fun updateProfileImage(customerId: Int, imageUri: Uri, context: Context) {
        viewModelScope.launch {
            customerRepository.uploadProfileImage(customerId, imageUri, context)?.let { imageUrl ->
                customerRepository.updateProfileImageUrl(customerId, imageUrl)
                fetchCustomerById(customerId) // Refresh data
            }
        }
    }
}

class CustomerViewModelFactory(
    private val customerRepository: CustomerRepository,
    private val materialRepository: MaterialRepository,
    private val inventoryRepository: InventoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CustomerViewModel(customerRepository, materialRepository, inventoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
