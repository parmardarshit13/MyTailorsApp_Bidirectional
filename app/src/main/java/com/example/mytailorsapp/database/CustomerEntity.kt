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
    val userType: String = "customer", // Default value
    val isLoggedIn: Boolean = false,
    val profileImage: String? = null, // Added for future profile pictures
    val createdAt: Long = System.currentTimeMillis() // Added for tracking
) {
    // Helper function to update login status
    fun withLoginStatus(loggedIn: Boolean): CustomerEntity {
        return this.copy(isLoggedIn = loggedIn)
    }
}