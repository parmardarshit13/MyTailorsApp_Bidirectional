package com.example.mytailorsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val contact: String,
    val email: String,
    val address: String,
    val password: String,
    val userType: String,
    val isLoggedIn: Boolean = false
)
