package com.example.mytailorsapp.data.models

data class WorkerEntity(
    val id: String = "",
    val name: String = "",
    val email: String = "",  // 🔹 Worker Email for Login
    val password: String = "",  // 🔹 Worker Password for Authentication
    val skills: String = "",  // 🔹 Specialization (e.g., Shirt, Pant, Suit)
    val isLoggedIn: Boolean = false,  // 🔹 Login Status
    val completedOrders: Int = 0,
    val pendingOrders: Int = 0
)
