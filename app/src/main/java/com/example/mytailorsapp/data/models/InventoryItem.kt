package com.example.mytailorsapp.data.models

import com.google.firebase.firestore.DocumentId
import java.io.Serializable
import java.util.*

data class InventoryItem(
    @DocumentId
    val id: String? = null,
    val customerId: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val customerAddress: String = "",

    val workerId: String = "",
    val workerName: String = "",

    val itemType: String = "", // E.g., Shirt, Pant, Blazer
    val itemDescription: String = "", // E.g., "Formal black pant", or notes

    val quantity: Int = 1,         // Number of garments
    val pricePerItem: Double = 0.0,
    val totalPrice: Double = 0.0,  // quantity * pricePerItem

    val status: InventoryStatus = InventoryStatus.IN_PROGRESS,

    val deadline: Date? = null,       // Stitching deadline
    val createdDate: Date = Date()    // Automatically assigned when created
) : Serializable {
    // Firebase requires an empty constructor
    constructor() : this(
        id = "",
        customerId = "",
        customerName = "",
        customerPhone = "",
        customerAddress = "",
        workerId = "",
        workerName = "",
        itemType = "",
        itemDescription = "",
        quantity = 0,
        pricePerItem = 0.0,
        totalPrice = 0.0,
        status = InventoryStatus.PENDING,
        deadline = null,
        createdDate = Date()
    )
}

enum class InventoryStatus {
    IN_PROGRESS,
    COMPLETED,
    PENDING,
    CANCELLED
}
