package com.example.mytailorsapp.data.repository

import com.example.mytailorsapp.data.models.Category
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CategoryRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val categoryCollection = firestore.collection("categories")

    suspend fun getAllCategories(): List<Category> {
        return try {
            val snapshot = categoryCollection.get().await()
            snapshot.documents.mapNotNull { it.toObject(Category::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
