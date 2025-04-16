package com.example.mytailorsapp.data.models

data class WorkerEntity(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",  // 🔹 Worker Email for Login
    val password: String = "",  // 🔹 Worker Password for Authentication
    val phoneNumber: String = "",  // 🔹 Contact Number
    val address: String = "",  // 🔹 Worker Address
    val experience: Int = 0,  // 🔹 Experience in Years
    val skills: String = "",  // 🔹 Specialization (e.g., Shirt, Pant, Suit)
    val userType: String = "worker",  // 🔹 Default User Type (to distinguish from customers)
    val isLoggedIn: Boolean = false,  // 🔹 Login Status
    val completedOrders: Int = 0,
    val pendingOrders: Int = 0
)
