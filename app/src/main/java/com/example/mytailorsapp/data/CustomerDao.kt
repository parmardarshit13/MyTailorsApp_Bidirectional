package com.example.mytailorsapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mytailorsapp.database.CustomerEntity

@Dao
interface CustomerDao {

    // 🔹 Register a new customer
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerCustomer(customer: CustomerEntity)

    // 🔹 Get all customers
    @Query("SELECT * FROM customers ORDER BY id ASC")
    suspend fun getAllCustomers(): List<CustomerEntity>

    // 🔹 Get customer by ID
    @Query("SELECT * FROM customers WHERE id = :customerId LIMIT 1")
    suspend fun getCustomerById(customerId: Int): CustomerEntity?

    // 🔹 Check if an email is already registered (Prevents duplicate registration)
    @Query("SELECT * FROM customers WHERE email = :email LIMIT 1")
    suspend fun getCustomerByEmail(email: String): CustomerEntity?

    // 🔹 Get logged-in customer ID
    @Query("SELECT id FROM customers WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInCustomerId(): Int?

    // ✅ Check user credentials during login
    @Query("SELECT * FROM customers WHERE name = :name AND password = :password LIMIT 1")
    suspend fun authenticateUser(name: String, password: String): CustomerEntity?

    // ✅ Retrieve a user by username (for forgot password or profile)
    @Query("SELECT * FROM customers WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): CustomerEntity?

    // ✅ Fix: Ensure update query runs correctly in Room
    @Query("UPDATE customers SET password = :newPassword WHERE name = :newName")
    suspend fun updatePassword(newName: String, newPassword: String)

    // ✅ Update login status after successful login
    @Query("UPDATE customers SET isLoggedIn = :isLoggedIn WHERE email = :email")
    suspend fun updateLoginStatus(email: String, isLoggedIn: Boolean)

    // 🔹 Update customer details
    @Update
    suspend fun updateCustomer(customer: CustomerEntity)
}
