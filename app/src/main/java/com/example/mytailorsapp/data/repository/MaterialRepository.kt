package com.example.mytailorsapp.data.repository

import com.example.mytailorsapp.data.models.MaterialItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MaterialRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val materialCollection = firestore.collection("materials")

    suspend fun insertMaterial(item: MaterialItem) {
        materialCollection.add(item).await()
    }

    suspend fun updateMaterial(item: MaterialItem) {
        val snapshot = materialCollection.whereEqualTo("id", item.id).get().await()
        snapshot.documents.firstOrNull()?.reference?.set(item)?.await()
    }

    suspend fun deleteMaterial(id: String) {
        val snapshot = materialCollection.whereEqualTo("id", id).get().await()
        snapshot.documents.firstOrNull()?.reference?.delete()?.await()
    }

    suspend fun getAllMaterials(): List<MaterialItem> {
        val snapshot = materialCollection.get().await()
        return snapshot.toObjects(MaterialItem::class.java)
    }

    suspend fun getMaterialsByCategory(category: String): List<MaterialItem> {
        val snapshot = materialCollection
            .whereEqualTo("category", category)
            .get()
            .await()
        return snapshot.toObjects(MaterialItem::class.java)
    }
}
