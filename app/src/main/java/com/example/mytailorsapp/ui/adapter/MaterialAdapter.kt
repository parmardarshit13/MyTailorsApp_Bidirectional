package com.example.mytailorsapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.MaterialItem
import com.example.mytailorsapp.ui.customer.MaterialDetailActivity

class MaterialAdapter : RecyclerView.Adapter<MaterialAdapter.ViewHolder>() {

    private val items = mutableListOf<MaterialItem>()

    fun submitList(newList: List<MaterialItem>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvMaterialName)
        val image: ImageView = view.findViewById(R.id.imgMaterial)

        fun bind(item: MaterialItem) {
            name.text = item.name
            image.load(item.image) {
                placeholder(R.drawable.ic_image_placeholder)
            }

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, MaterialDetailActivity::class.java).apply {
                    putExtra("name", item.name)
                    putExtra("category", item.category)
                    putExtra("price", item.price)
                    putExtra("imageUrl", item.image)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_material, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
