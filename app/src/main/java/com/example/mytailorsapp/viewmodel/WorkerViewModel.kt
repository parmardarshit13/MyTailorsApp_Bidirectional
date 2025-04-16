package com.example.mytailorsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytailorsapp.data.models.WorkerEntity
import com.example.mytailorsapp.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkerViewModel(private val workerRepository: WorkerRepository) : ViewModel() {

    // 🔹 Holds worker data as StateFlow (better for Jetpack Compose)
    private val _selectedWorker = MutableStateFlow<WorkerEntity?>(null)
    val selectedWorker: StateFlow<WorkerEntity?> get() = _selectedWorker

    // 🔹 Holds all workers as Flow
    private val _allWorkers = MutableStateFlow<List<WorkerEntity>>(emptyList())
    val allWorkers: StateFlow<List<WorkerEntity>> get() = _allWorkers

    // 🔹 Fetch all workers from DB
    fun fetchAllWorkers() {
        viewModelScope.launch {
            _allWorkers.value = workerRepository.getAllWorkers()
        }
    }

    // 🔹 Fetch worker by ID
    fun fetchWorkerById(workerId: Int) {
        viewModelScope.launch {
            val worker = workerRepository.getWorkerById(workerId)
            _selectedWorker.value = worker  // Update UI StateFlow
        }
    }

    // 🔹 Insert a new worker
    fun insertWorker(worker: WorkerEntity) {
        viewModelScope.launch {
            workerRepository.insertWorker(worker)
            fetchAllWorkers()  // Refresh worker list after insertion
        }
    }

    // 🔹 Delete all workers
    fun clearWorkers() {
        viewModelScope.launch {
            workerRepository.deleteAllWorkers()
            _allWorkers.value = emptyList()  // Clear UI StateFlow
        }
    }

    // 🔹 Update worker details
    fun updateWorker(worker: WorkerEntity) {
        viewModelScope.launch {
            workerRepository.updateWorker(worker)
            _selectedWorker.value = worker  // Notify UI
        }
    }
}

// 🔹 ViewModel Factory to provide WorkerDao
class WorkerViewModelFactory(private val workerRepository: WorkerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkerViewModel(workerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
