package com.example.mytailorsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytailorsapp.data.models.WorkerEntity
import com.example.mytailorsapp.data.repository.WorkerRepository
import kotlinx.coroutines.launch

class WorkerViewModel(private val workerRepository: WorkerRepository) : ViewModel() {

    // Use LiveData for XML compatibility
    private val _selectedWorker = MutableLiveData<WorkerEntity?>()
    val selectedWorker: LiveData<WorkerEntity?> get() = _selectedWorker

    private val _allWorkers = MutableLiveData<List<WorkerEntity>>()
    val allWorkers: LiveData<List<WorkerEntity>> get() = _allWorkers

    // Fetch all workers from DB
    fun fetchAllWorkers() {
        viewModelScope.launch {
            _allWorkers.value = workerRepository.getAllWorkers()
        }
    }

    // Fetch worker by ID
    fun fetchWorkerByName(workerName: String) {
        viewModelScope.launch {
            val worker = workerRepository.getWorkerByName(workerName)
            _selectedWorker.value = worker  // Update LiveData
        }
    }

    // Insert a new worker
    fun insertWorker(worker: WorkerEntity) {
        viewModelScope.launch {
            workerRepository.insertWorker(worker)
            fetchAllWorkers()  // Refresh worker list after insertion
        }
    }

    // Delete all workers
    fun clearWorkers() {
        viewModelScope.launch {
            workerRepository.deleteAllWorkers()
            _allWorkers.value = emptyList()  // Clear LiveData
        }
    }

    // Update worker details
    fun updateWorker(worker: WorkerEntity) {
        viewModelScope.launch {
            workerRepository.updateWorker(worker)
            _selectedWorker.value = worker  // Notify LiveData
        }
    }
}

// ViewModel Factory to provide WorkerRepository
class WorkerViewModelFactory(private val workerRepository: WorkerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkerViewModel(workerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
