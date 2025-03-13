package com.example.mytailorsapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mytailorsapp.database.AdminEntity

@Dao
interface AdminDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: AdminEntity)

    @Query("SELECT * FROM admins WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getAdmin(username: String, password: String): AdminEntity?
}
