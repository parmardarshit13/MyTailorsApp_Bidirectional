package com.example.mytailorsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytailorsapp.database.AdminEntity
import com.example.mytailorsapp.database.AppDatabase
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application).adminDao()

    fun insertAdmin(admin: AdminEntity) {
        viewModelScope.launch {
            db.insertAdmin(admin)
        }
    }

    fun authenticateAdmin(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val admin = db.getAdmin(username, password)
            onResult(admin != null)
        }
    }
}
