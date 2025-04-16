package com.example.mytailorsapp.data.repository

import com.example.mytailorsapp.data.models.WorkerEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class WorkerRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val workersCollection = firestore.collection("workers")

    suspend fun authenticateWorker(email: String, password: String): WorkerEntity? {
        val snapshot = workersCollection
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get().await()

        return snapshot.documents.firstOrNull()?.toObject(WorkerEntity::class.java)
    }

    suspend fun getWorkerById(id: Int): WorkerEntity? {
        val snapshot = workersCollection.whereEqualTo("id", id).get().await()
        return snapshot.toObjects(WorkerEntity::class.java).firstOrNull()
    }

    suspend fun updateLoginStatus(email: String, status: Boolean) {
        val snapshot = workersCollection.whereEqualTo("email", email).get().await()
        snapshot.documents.firstOrNull()?.reference?.update("isLoggedIn", status)?.await()
    }

    suspend fun getAllWorkers(): List<WorkerEntity> {
        val snapshot = workersCollection.get().await()
        return snapshot.toObjects(WorkerEntity::class.java)
    }

    suspend fun insertWorker(worker: WorkerEntity) {
        workersCollection.add(worker).await()
    }

    suspend fun deleteWorker(workerId: Int) {
        val snapshot = workersCollection.whereEqualTo("id", workerId).get().await()
        snapshot.documents.firstOrNull()?.reference?.delete()?.await()
    }

    suspend fun deleteAllWorkers() {
        val workers = getAllWorkers()
        workers.forEach {
            deleteWorker(it.id)
        }
    }

    suspend fun updateWorker(worker: WorkerEntity) {
        val snapshot = workersCollection
            .whereEqualTo("id", worker.id)
            .get()
            .await()

        snapshot.documents.firstOrNull()?.let {
            workersCollection.document(it.id).set(worker).await()
        }
    }
}
