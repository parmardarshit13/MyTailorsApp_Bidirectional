package com.example.mytailorsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.WorkerEntity

class WorkerAdapter : RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder>() {

    private val workers = mutableListOf<WorkerEntity>()

    fun submitList(list: List<WorkerEntity>) {
        workers.clear()
        workers.addAll(list)
        notifyDataSetChanged()
    }

    inner class WorkerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvWorkerName)
        val tvEmail: TextView = view.findViewById(R.id.tvWorkerEmail)
        val tvSkills: TextView = view.findViewById(R.id.tvWorkerSkills)
        val tvOrders: TextView = view.findViewById(R.id.tvWorkerOrders)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_worker, parent, false)
        return WorkerViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int) {
        val worker = workers[position]
        holder.tvName.text = worker.name
        holder.tvEmail.text = "Email: ${worker.email}"
        holder.tvSkills.text = "Skills: ${worker.skills}"
        holder.tvOrders.text = "Completed: ${worker.completedOrders} | Pending: ${worker.pendingOrders}"
    }

    override fun getItemCount(): Int = workers.size
}
