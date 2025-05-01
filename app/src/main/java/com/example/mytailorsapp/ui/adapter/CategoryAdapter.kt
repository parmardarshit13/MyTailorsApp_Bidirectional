package com.example.mytailorsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.Category

class CategoryAdapter(
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val categories = mutableListOf<Category>()

    fun submitList(list: List<Category>) {
        categories.clear()
        categories.addAll(list)
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCategory: ImageView = itemView.findViewById(R.id.imgCategory)
        val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)

        fun bind(category: Category) {
            tvCategoryName.text = category.name
            imgCategory.load(category.imageUrl)
            itemView.setOnClickListener {
                onItemClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}
