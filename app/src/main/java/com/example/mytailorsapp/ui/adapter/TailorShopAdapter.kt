package com.example.mytailorsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.TailorShop

class TailorShopAdapter(
    private val shops: List<TailorShop>
) : RecyclerView.Adapter<TailorShopAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvShopName)
        val tvLocation: TextView = view.findViewById(R.id.tvShopLocation)
        val tvPrice: TextView = view.findViewById(R.id.tvShopPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shop_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shop = shops[position]
        holder.tvName.text = shop.name
        holder.tvLocation.text = shop.location
        holder.tvPrice.text = shop.pricePerItem
    }

    override fun getItemCount(): Int = shops.size
}
