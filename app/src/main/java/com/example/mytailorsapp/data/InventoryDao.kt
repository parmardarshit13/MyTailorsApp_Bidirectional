package com.example.mytailorsapp.data

import androidx.room.*
import com.example.mytailorsapp.database.InventoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {

    // Get all inventory items as Flow (Live updates)
    @Query("SELECT * FROM inventory_items")
    fun getAllInventoryItems(): Flow<List<InventoryItem>>

    // Insert new item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryItem(item: InventoryItem)

    // Update existing item
    @Update
    suspend fun updateInventoryItem(item: InventoryItem)

    // Delete an item
    @Delete
    suspend fun deleteInventoryItem(item: InventoryItem)

    @Query("SELECT * FROM inventory_items WHERE id = :id")
    fun getInventoryItemById(id: Int): Flow<InventoryItem?>

    @Query("SELECT * FROM inventory_items WHERE name = :name")
    fun getInventoryItemByName(name: String): Flow<InventoryItem?>

    @Query("SELECT * FROM inventory_items WHERE customerId = :customerId")
    fun getInventoryByCustomerId(customerId: Int): Flow<List<InventoryItem>>
}
