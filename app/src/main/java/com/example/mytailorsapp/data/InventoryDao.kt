package com.example.mytailorsapp.data

import androidx.room.Dao
import androidx.room.Query
import com.example.mytailorsapp.database.InventoryItem

@Dao
interface InventoryDao {

    @Query("SELECT * FROM inventory_items")
    suspend fun getAllInventoryItems(): List<InventoryItem>
}
