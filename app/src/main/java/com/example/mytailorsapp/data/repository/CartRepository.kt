package com.example.mytailorsapp.data.repository

import com.example.mytailorsapp.data.models.CartItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CartRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val cartCollection = firestore.collection("cart_items")

    suspend fun addToCart(item: CartItem) {
        cartCollection.add(item).await()
    }

    suspend fun getCartItems(customerId: String): List<CartItem> {
        val snapshot = cartCollection
            .whereEqualTo("customerId", customerId)
            .get()
            .await()
        return snapshot.toObjects(CartItem::class.java)
    }

    suspend fun updateCartItemMeters(itemId: String, newMeters: Int) {
        val snapshot = cartCollection.whereEqualTo("id", itemId).get().await()
        val document = snapshot.documents.firstOrNull()
        document?.reference?.update("meters", newMeters)?.await()
    }

    suspend fun removeCartItem(itemId: String) {
        val snapshot = cartCollection.whereEqualTo("id", itemId).get().await()
        val document = snapshot.documents.firstOrNull()
        document?.reference?.delete()?.await()
    }

    suspend fun clearCart(customerId: String) {
        val items = getCartItems(customerId)
        items.forEach { removeCartItem(it.id) }
    }
}
