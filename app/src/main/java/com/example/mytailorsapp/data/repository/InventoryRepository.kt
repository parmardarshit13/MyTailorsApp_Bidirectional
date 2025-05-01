package com.example.mytailorsapp.data.repository

import com.example.mytailorsapp.data.models.InventoryItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class InventoryRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val adminInventoryRef = firestore.collection("admin_inventory_item")

    // ✅ Fetch all items
    suspend fun getAllAdminInventory(): List<InventoryItem> {
        return try {
            val snapshot = adminInventoryRef.get().await()
            snapshot.toObjects(InventoryItem::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // ✅ Insert a new item
    suspend fun insertInventoryItem(item: InventoryItem) {
        try {
            val docRef = adminInventoryRef.add(item).await()
            docRef.update("documentId", docRef.id).await() // store the ID inside document
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ✅ Update item by matching ID
    suspend fun updateInventoryItem(item: InventoryItem) {
        try {
            item.id?.let {
                adminInventoryRef.document(it).set(item).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ✅ Delete item by ID
    suspend fun deleteInventoryItem(item: InventoryItem): Boolean {
        return try {
            item.id?.let {
                adminInventoryRef.document(it).delete().await()
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ✅ Fetch by Customer Name (for searching)
    suspend fun getInventoryItemByCustomerName(name: String): List<InventoryItem> {
        return try {
            val snapshot = adminInventoryRef
                .whereEqualTo("customerName", name)
                .get()
                .await()
            snapshot.toObjects(InventoryItem::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // ✅ Fetch by Worker Name (for filter)
    suspend fun getInventoryByWorker(workerName: String): List<InventoryItem> {
        return try {
            val snapshot = adminInventoryRef
                .whereEqualTo("workerName", workerName)
                .get()
                .await()
            snapshot.toObjects(InventoryItem::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
