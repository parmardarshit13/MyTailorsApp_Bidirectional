package com.example.mytailorsapp.data

import androidx.room.*
import com.example.mytailorsapp.database.WorkerEntity

@Dao
interface WorkerDao {

    // 🔹 Fetch all workers
    @Query("SELECT * FROM workers")
    suspend fun getAllWorkers(): List<WorkerEntity>

    // 🔹 Fetch a single worker by ID
    @Query("SELECT * FROM workers WHERE id = :workerId LIMIT 1")
    suspend fun getWorkerById(workerId: Int): WorkerEntity?

    // 🔹 Fetch a worker by Email & Password (For Login)
    @Query("SELECT * FROM workers WHERE email = :email AND password = :password LIMIT 1")
    suspend fun authenticateWorker(email: String, password: String): WorkerEntity?

    // 🔹 Check if Email already exists (For Registration)
    @Query("SELECT * FROM workers WHERE email = :email LIMIT 1")
    suspend fun getWorkerByEmail(email: String): WorkerEntity?

    // 🔹 Update Login Status
    @Query("UPDATE workers SET isLoggedIn = :status WHERE email = :email")
    suspend fun updateLoginStatus(email: String, status: Boolean)

    // 🔹 Update Completed Orders Count
    @Query("UPDATE workers SET completedOrders = completedOrders + :count WHERE id = :workerId")
    suspend fun updateCompletedOrders(workerId: Int, count: Int)

    // 🔹 Update Pending Orders Count
    @Query("UPDATE workers SET pendingOrders = pendingOrders + :count WHERE id = :workerId")
    suspend fun updatePendingOrders(workerId: Int, count: Int)

    // 🔹 Insert or update a worker
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorker(worker: WorkerEntity)

    // 🔹 Update an existing worker
    @Update
    suspend fun updateWorker(worker: WorkerEntity)

    // 🔹 Delete a specific worker by ID
    @Query("DELETE FROM workers WHERE id = :workerId")
    suspend fun deleteWorker(workerId: Int)

    // 🔹 Delete all workers
    @Query("DELETE FROM workers")
    suspend fun deleteAllWorkers()
}
