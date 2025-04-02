package com.example.mytailorsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_items")
data class InventoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerId: Int,
    val name: String,
    val type: String,
    val price: Double,
    val status: InventoryStatus   // ✅ Enum instead of String
)

enum class InventoryStatus {
    IN_PROGRESS, COMPLETED, PENDING
}
