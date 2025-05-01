package com.example.mytailorsapp.data.repository

import com.example.mytailorsapp.data.models.WishlistItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class WishlistRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("wishlist")

    suspend fun addToWishlist(item: WishlistItem) {
        collection.add(item).await()
    }

    suspend fun getWishlist(customerId: String): List<WishlistItem> {
        val snapshot = collection.whereEqualTo("customerId", customerId).get().await()
        return snapshot.toObjects(WishlistItem::class.java)
    }

    suspend fun removeFromWishlist(documentId: String, customerId: String) {
        val snapshot = collection
            .whereEqualTo("customerId", customerId)
            .whereEqualTo("id", documentId)
            .get()
            .await()

        val document = snapshot.documents.firstOrNull()
        document?.reference?.delete()?.await()
    }
}
