package com.example.mytailorsapp.data.repository

import com.example.mytailorsapp.data.models.InventoryItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class InventoryRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val adminCollection = firestore.collection("admin_inventory_item")
    private val customerCollection = firestore.collection("customer_inventory_item")

    suspend fun getAllAdminInventory(): List<InventoryItem> {
        val snapshot = adminCollection.get().await()
        return snapshot.toObjects(InventoryItem::class.java)
    }

    suspend fun getCustomerInventory(customerId: Int): List<InventoryItem> {
        val snapshot = customerCollection
            .whereEqualTo("customerId", customerId)
            .get().await()
        return snapshot.toObjects(InventoryItem::class.java)
    }

    suspend fun insertInventoryItem(item: InventoryItem, isAdmin: Boolean) {
        val collection = if (isAdmin) adminCollection else customerCollection
        collection.add(item).await()
    }

    suspend fun updateInventoryItem(item: InventoryItem, isAdmin: Boolean) {
        val collection = if (isAdmin) adminCollection else customerCollection
        val query = collection.whereEqualTo("id", item.id).get().await()
        val document = query.documents.firstOrNull()
        document?.reference?.set(item)?.await()
    }

    suspend fun deleteInventoryItem(item: InventoryItem, isAdmin: Boolean) {
        val collection = if (isAdmin) adminCollection else customerCollection
        val query = collection.whereEqualTo("id", item.id).get().await()
        val document = query.documents.firstOrNull()
        document?.reference?.delete()?.await()
    }

    suspend fun getInventoryItemByName(name: String, isAdmin: Boolean): InventoryItem? {
        val collection = if (isAdmin) adminCollection else customerCollection
        val snapshot = collection.whereEqualTo("name", name).get().await()
        return snapshot.documents.firstOrNull()?.toObject(InventoryItem::class.java)
    }
}
