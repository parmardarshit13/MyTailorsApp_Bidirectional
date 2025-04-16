package com.example.mytailorsapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytailorsapp.data.models.AdminEntity
import com.example.mytailorsapp.data.repository.AdminRepository
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    var adminDetails by mutableStateOf<AdminEntity?>(null)
        private set

    fun insertAdmin(admin: AdminEntity) {
        viewModelScope.launch {
            // Firebase auto-generates IDs; typically you'd use `add()` if needed
            // This is a placeholder in case you implement add functionality in repository
        }
    }

    fun authenticateAdmin(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val admin = repository.getAdmin(username, password)
            if (admin != null) {
                repository.updateLoginStatus(true)
                adminDetails = admin
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.updateLoginStatus(false)
            adminDetails = null
        }
    }

    fun fetchLoggedInAdmin() {
        viewModelScope.launch {
            adminDetails = repository.getLoggedInAdmin()
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
