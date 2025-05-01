package com.example.mytailorsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.CartItem

class CartAdapter(
    private val onQuantityChange: () -> Unit,
    private val onDelete: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private val cartItems = mutableListOf<CartItem>()

    fun submitList(items: List<CartItem>) {
        cartItems.clear()
        cartItems.addAll(items)
        notifyDataSetChanged()
    }

    fun calculateTotal(): Double {
        return cartItems.sumOf { it.totalPrice }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val img: ImageView = view.findViewById(R.id.imgMaterialCart)
        private val name: TextView = view.findViewById(R.id.tvCartMaterialName)
        private val price: TextView = view.findViewById(R.id.tvCartPricePerMeter)
        private val total: TextView = view.findViewById(R.id.tvCartTotalPrice)
        private val btnPlus: ImageButton = view.findViewById(R.id.btnPlusMeter)
        private val btnMinus: ImageButton = view.findViewById(R.id.btnMinusMeter)
        private val meterText: TextView = view.findViewById(R.id.tvCartMeterCount)
        private val btnDelete: ImageButton = view.findViewById(R.id.btnRemoveCart)

        fun bind(cartItem: CartItem) {
            name.text = cartItem.name
            price.text = "₹${cartItem.pricePerMeter} / meter"
            meterText.text = cartItem.meters.toString()
            total.text = "Total: ₹${cartItem.totalPrice}"
            img.load(cartItem.imageUrl)

            btnPlus.setOnClickListener {
                val newMeters = cartItem.meters + 1
                cartItems[adapterPosition] = cartItem.copy(meters = newMeters)
                notifyItemChanged(adapterPosition)
                onQuantityChange()
            }

            btnMinus.setOnClickListener {
                if (cartItem.meters > 1) {
                    val newMeters = cartItem.meters - 1
                    cartItems[adapterPosition] = cartItem.copy(meters = newMeters)
                    notifyItemChanged(adapterPosition)
                    onQuantityChange()
                }
            }

            // Long press to delete with confirmation
            btnDelete.setOnClickListener {
                AlertDialog.Builder(it.context)
                    .setTitle("Remove item?")
                    .setMessage("Are you sure you want to remove ${cartItem.name} from your cart?")
                    .setPositiveButton("Remove") { _, _ ->
                        onDelete(cartItem)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = cartItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }
}
