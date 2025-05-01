package com.example.mytailorsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytailorsapp.data.models.AdminEntity
import com.example.mytailorsapp.data.repository.AdminRepository
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    private val _adminDetails = MutableLiveData<AdminEntity?>()
    val adminDetails: LiveData<AdminEntity?> get() = _adminDetails

    fun authenticateAdmin(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val admin = repository.loginAdmin(username, password)
            if (admin != null) {
                _adminDetails.value = admin
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.updateLoginStatus(false)
            _adminDetails.value = null
        }
    }

    fun fetchLoggedInAdmin() {
        viewModelScope.launch {
            _adminDetails.value = repository.getLoggedInAdmin()
        }
    }
}

class AdminViewModelFactory(
    private val repository: AdminRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
