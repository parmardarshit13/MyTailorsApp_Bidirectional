package com.example.mytailorsapp.data

import androidx.room.*
import com.example.mytailorsapp.database.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    // 🔹 Insert Customer (For Registration)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerCustomer(customer: CustomerEntity)

    // 🔹 Authentication & Login
    @Query("SELECT * FROM customers WHERE email = :email AND password = :password LIMIT 1")
    suspend fun authenticate(email: String, password: String): CustomerEntity?

    @Query("UPDATE customers SET isLoggedIn = :isLoggedIn WHERE email = :email")
    suspend fun updateLoginStatus(email: String, isLoggedIn: Boolean)

    @Query("SELECT id FROM customers WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInCustomerId(): Int?

    @Query("UPDATE customers SET isLoggedIn = 0")
    suspend fun logoutAllCustomers()

    // 🔹 Profile Management
    @Query("UPDATE customers SET name = :name, contact = :contact, email = :email, address = :address WHERE id = :customerId")
    suspend fun updateProfileDetails(customerId: Int, name: String, contact: String, email: String, address: String)

    @Query("UPDATE customers SET password = :newPassword WHERE email = :email")
    suspend fun updatePassword(email: String, newPassword: String)

    // 🔹 Fetch Customer Data
    @Query("SELECT * FROM customers WHERE id = :customerId LIMIT 1")
    suspend fun getCustomerById(customerId: Int): CustomerEntity?

    @Query("SELECT * FROM customers WHERE email = :email LIMIT 1")
    suspend fun getCustomerByEmail(email: String): CustomerEntity?

    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomersFlow(): Flow<List<CustomerEntity>>

    // 🔹 Utility Queries
    @Query("SELECT COUNT(*) FROM customers WHERE email = :email")
    suspend fun emailExists(email: String): Int
}
