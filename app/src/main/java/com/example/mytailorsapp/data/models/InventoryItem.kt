package com.example.mytailorsapp.data.models

data class InventoryItem(
    val id: Int = 0,
    val customerId: Int? = null,
    val name: String = "",
    val type: String = "",
    val price: Double,
    val status: InventoryStatus   // ✅ Enum instead of String
)

enum class InventoryStatus {
    IN_PROGRESS, COMPLETED, PENDING
}
