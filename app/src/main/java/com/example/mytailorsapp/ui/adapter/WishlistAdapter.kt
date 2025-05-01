package com.example.mytailorsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.WishlistItem

class WishlistAdapter(
    private val onItemClick: (WishlistItem) -> Unit,
    private val onDeleteClick: (WishlistItem) -> Unit
) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

    private val items = mutableListOf<WishlistItem>()

    fun submitList(list: List<WishlistItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val img: ImageView = view.findViewById(R.id.imgMaterial)
        private val name: TextView = view.findViewById(R.id.tvName)
        private val category: TextView = view.findViewById(R.id.tvCategory)
        private val price: TextView = view.findViewById(R.id.tvPricePerMeter)
        private val btnDelete: ImageButton = view.findViewById(R.id.btnRemove)

        fun bind(item: WishlistItem) {
            name.text = item.name
            category.text = item.category
            price.text = item.pricePerMeter.toString()
            img.load(item.imageUrl) {
                placeholder(R.drawable.ic_image_placeholder)
            }

            itemView.setOnClickListener { onItemClick(item) }

            btnDelete.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wishlist, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
