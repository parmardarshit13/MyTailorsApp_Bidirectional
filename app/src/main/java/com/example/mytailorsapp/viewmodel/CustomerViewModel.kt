package com.example.mytailorsapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytailorsapp.data.models.CartItem
import com.example.mytailorsapp.data.models.CustomerEntity
import com.example.mytailorsapp.data.models.MaterialItem
import com.example.mytailorsapp.data.models.WishlistItem
import com.example.mytailorsapp.data.repository.CartRepository
import com.example.mytailorsapp.data.repository.CustomerRepository
import com.example.mytailorsapp.data.repository.MaterialRepository
import com.example.mytailorsapp.data.repository.WishlistRepository
import kotlinx.coroutines.launch

class CustomerViewModel(
    internal val customerRepository: CustomerRepository,
    private val materialRepository: MaterialRepository,
    private val cartRepository: CartRepository,
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    private val _selectedCustomer = MutableLiveData<CustomerEntity?>()
    val selectedCustomer: LiveData<CustomerEntity?> = _selectedCustomer

    private val _materialItems = MutableLiveData<List<MaterialItem>>()

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    init {
        fetchAllMaterials()
    }

    private fun fetchAllMaterials() {
        viewModelScope.launch {
            val materials = materialRepository.getAllMaterials()
            _materialItems.postValue(materials)
        }
    }

    fun fetchCustomerById(customerId: String) {
        viewModelScope.launch {
            val customer = customerRepository.getCustomerById(customerId)
            _selectedCustomer.postValue(customer)
        }
    }

    suspend fun login(email: String, password: String): CustomerEntity? {
        customerRepository.logoutAllCustomers() // ✅ logout everyone first
        val customer = customerRepository.login(email, password)
        customer?.let {
            customerRepository.updateLoginStatus(it.email, true)
            _selectedCustomer.value = it
        }
        return customer
    }

    fun getLoggedInCustomerId(context: Context, callback: (String?) -> Unit) {
        viewModelScope.launch {
            val id = customerRepository.getLoggedInCustomerId(context)
            callback(id)
        }
    }

    fun updateCustomerProfile(customer: CustomerEntity) {
        viewModelScope.launch {
            customerRepository.updateCustomerProfile(customer)
            _selectedCustomer.postValue(customer)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _selectedCustomer.value?.let {
                customerRepository.logout(it.email)
                _selectedCustomer.postValue(null)
            }
        }
    }

    fun addToCart(item: CartItem) {
        viewModelScope.launch {
            cartRepository.addToCart(item)
            fetchCartItems(item.customerId)
        }
    }

    fun fetchCartItems(customerId: String) {
        viewModelScope.launch {
            val items = cartRepository.getCartItems(customerId)
            _cartItems.postValue(items)
        }
    }

    fun removeCartItem(itemId: String, customerId: String) {
        viewModelScope.launch {
            cartRepository.removeCartItem(itemId)
            fetchCartItems(customerId)
        }
    }

    fun addToWishlist(item: WishlistItem) {
        viewModelScope.launch {
            wishlistRepository.addToWishlist(item)
        }
    }

    fun fetchWishlist(customerId: String, callback: (List<WishlistItem>) -> Unit) {
        viewModelScope.launch {
            val list = wishlistRepository.getWishlist(customerId)
            callback(list)
        }
    }

    fun removeFromWishlist(documentId: String, customerId: String) {
        viewModelScope.launch {
            wishlistRepository.removeFromWishlist(documentId, customerId)
            fetchWishlist(customerId) {}
        }
    }
}

class CustomerViewModelFactory(
    private val customerRepository: CustomerRepository,
    private val materialRepository: MaterialRepository,
    private val cartRepository: CartRepository,
    private val wishlistRepository: WishlistRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CustomerViewModel(customerRepository, materialRepository,
                cartRepository, wishlistRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
