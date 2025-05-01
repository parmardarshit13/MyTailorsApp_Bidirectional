package com.example.mytailorsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.InventoryItem
import java.text.SimpleDateFormat
import java.util.*

class InventoryAdapter(
    private val onItemClick: (InventoryItem) -> Unit,
    private val onItemLongClick: (InventoryItem) -> Unit
) : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    private val items = mutableListOf<InventoryItem>()

    fun submitList(list: List<InventoryItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvCustomerName: TextView = view.findViewById(R.id.tvInventoryCustomerName)
        private val tvWorkerName: TextView = view.findViewById(R.id.tvInventoryWorkerName)
        private val tvQuantityAndTotal: TextView = view.findViewById(R.id.tvInventoryGarments)
        private val tvStatus: TextView = view.findViewById(R.id.tvInventoryStatus)
        private val tvDeadline: TextView? = view.findViewById(R.id.tvInventoryDeadline) // optional if added

        fun bind(item: InventoryItem) {
            tvCustomerName.text = "Customer: ${item.customerName}"
            tvWorkerName.text = "Worker: ${item.workerName}"
            tvQuantityAndTotal.text = "Qty: ${item.quantity} | Total: ₹${item.totalPrice}"
            tvStatus.text = "Status: ${item.status}"

            tvDeadline?.text = item.deadline?.let {
                "Deadline: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it)}"
            } ?: "Deadline: N/A"

            itemView.setOnClickListener { onItemClick(item) }
            itemView.setOnLongClickListener {
                onItemLongClick(item)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventory, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
