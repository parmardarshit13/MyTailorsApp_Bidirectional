//package com.example.mytailorsapp.repository
//
//import androidx.lifecycle.LiveData
//    import androidx.lifecycle.MutableLiveData
//    import com.example.mytailorsapp.ui.customer.Order
//    import com.example.mytailorsapp.ui.admin.Worker
//
//class TailorRepository {
//    private val _orders = MutableLiveData<List<Order>>(listOf(
//        Order("Order #1", "In Progress"),
//        Order("Order #2", "Completed")
//    ))
//    val orders: LiveData<List<Order>> get() = _orders
//
//    private val _workers = MutableLiveData<List<Worker>>(listOf(
//        Worker("John Doe", 5, 2),
//        Worker("Jane Smith", 3, 1)
//    ))
//    val workers: LiveData<List<Worker>> get() = _workers
//}
