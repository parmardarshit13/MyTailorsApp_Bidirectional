package com.example.mytailorsapp.data.models

data class AdminEntity(
    val id: Int = 0,
    val username: String = "",
    val password: String = "",
    val isLoggedIn: Boolean = false,
    val isAdmin: Boolean = true
)
