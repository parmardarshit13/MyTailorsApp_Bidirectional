package com.example.mytailorsapp.data.models

import com.google.firebase.firestore.DocumentId

data class CustomerEntity(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val contact: String = "",
    val email: String = "",
    val address: String = "",
    val password: String = "",
    val userType: String = "customer", // Default value
    val isLoggedIn: Boolean = false,
    val profileImageUrl: String? = "", // Added for future profile pictures
    val createdAt: Long = System.currentTimeMillis() // Added for tracking
)