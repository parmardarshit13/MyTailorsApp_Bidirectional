package com.example.mytailorsapp.data

import androidx.room.*
import com.example.mytailorsapp.database.WorkerEntity

@Dao
interface WorkerDao {

    // 🔹 Insert Worker (For Registration)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorker(worker: WorkerEntity)

    // 🔹 Authentication & Login
    @Query("SELECT * FROM workers WHERE email = :email AND password = :password LIMIT 1")
    suspend fun authenticateWorker(email: String, password: String): WorkerEntity?

    @Query("UPDATE workers SET isLoggedIn = :status WHERE email = :email")
    suspend fun updateLoginStatus(email: String, status: Boolean)

    // 🔹 Fetch Worker Data
    @Query("SELECT * FROM workers WHERE id = :workerId LIMIT 1")
    suspend fun getWorkerById(workerId: Int): WorkerEntity?

    @Query("SELECT * FROM workers WHERE email = :email LIMIT 1")
    suspend fun getWorkerByEmail(email: String): WorkerEntity?

    @Query("SELECT * FROM workers")
    suspend fun getAllWorkers(): List<WorkerEntity>

    // 🔹 Order Management
    @Query("UPDATE workers SET completedOrders = completedOrders + :count WHERE id = :workerId")
    suspend fun updateCompletedOrders(workerId: Int, count: Int)

    @Query("UPDATE workers SET pendingOrders = pendingOrders + :count WHERE id = :workerId")
    suspend fun updatePendingOrders(workerId: Int, count: Int)

    // 🔹 Update & Delete Operations
    @Update
    suspend fun updateWorker(worker: WorkerEntity)

    @Query("DELETE FROM workers WHERE id = :workerId")
    suspend fun deleteWorker(workerId: Int)
}
