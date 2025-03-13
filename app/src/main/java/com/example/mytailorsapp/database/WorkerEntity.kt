package com.example.mytailorsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workers")
data class WorkerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,  // 🔹 Worker Email for Login
    val password: String,  // 🔹 Worker Password for Authentication
    val phoneNumber: String,  // 🔹 Contact Number
    val address: String,  // 🔹 Worker Address
    val experience: Int,  // 🔹 Experience in Years
    val skills: String,  // 🔹 Specialization (e.g., Shirt, Pant, Suit)
    val userType: String = "worker",  // 🔹 Default User Type (to distinguish from customers)
    val isLoggedIn: Boolean = false,  // 🔹 Login Status
    val completedOrders: Int,
    val pendingOrders: Int
)
