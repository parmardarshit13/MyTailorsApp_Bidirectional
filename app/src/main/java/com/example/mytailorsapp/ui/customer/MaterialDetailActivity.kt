package com.example.mytailorsapp.ui.customer

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.mytailorsapp.R
import com.example.mytailorsapp.data.models.CartItem
import com.example.mytailorsapp.data.models.WishlistItem
import com.example.mytailorsapp.data.repository.CartRepository
import com.example.mytailorsapp.data.repository.CustomerRepository
import com.example.mytailorsapp.data.repository.InventoryRepository
import com.example.mytailorsapp.data.repository.MaterialRepository
import com.example.mytailorsapp.data.repository.WishlistRepository
import com.example.mytailorsapp.viewmodel.CustomerViewModel
import com.example.mytailorsapp.viewmodel.CustomerViewModelFactory

class MaterialDetailActivity : AppCompatActivity() {

    private var meterCount = 1
    private var pricePerMeter = 0.0
    private lateinit var viewModel: CustomerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_detail)

        // Set up ViewModel
        viewModel = ViewModelProvider(
            this,
            CustomerViewModelFactory(
                CustomerRepository(),
                MaterialRepository(),
                CartRepository(),
                WishlistRepository()
            )
        )[CustomerViewModel::class.java]

        val imgMaterial = findViewById<ImageView>(R.id.imgMaterialDetail)
        val tvName = findViewById<TextView>(R.id.tvMaterialNameDetail)
        val tvCategory = findViewById<TextView>(R.id.tvMaterialCategoryDetail)
        val tvPrice = findViewById<TextView>(R.id.tvMaterialPriceDetail)
        val tvMeterCount = findViewById<TextView>(R.id.tvMeterCount)
        val tvTotalPrice = findViewById<TextView>(R.id.tvTotalPrice)
        val btnMinus = findViewById<ImageButton>(R.id.btnMinus)
        val btnPlus = findViewById<ImageButton>(R.id.btnPlus)
        val btnAddToInventory = findViewById<ImageButton>(R.id.btnAddToInventory)
        val btnAddToCart = findViewById<Button>(R.id.btnAddToCart)

        val name = intent.getStringExtra("name") ?: "Material"
        val category = intent.getStringExtra("category") ?: "N/A"
        pricePerMeter = intent.getDoubleExtra("price", 0.0)
        val imageUrl = intent.getStringExtra("imageUrl")

        // Set UI with values
        tvName.text = name
        tvCategory.text = "Category: $category"
        tvPrice.text = "Price (per meter): ₹$pricePerMeter"
        tvMeterCount.text = meterCount.toString()
        tvTotalPrice.text = "Total: ₹${String.format("%.2f", meterCount * pricePerMeter)}"
        imgMaterial.load(imageUrl)

        // + button
        btnPlus.setOnClickListener {
            meterCount++
            tvMeterCount.text = meterCount.toString()
            tvTotalPrice.text = "Total: ₹${String.format("%.2f", meterCount * pricePerMeter)}"
        }

        // - button
        btnMinus.setOnClickListener {
            if (meterCount > 1) {
                meterCount--
                tvMeterCount.text = meterCount.toString()
                tvTotalPrice.text = "Total: ₹${String.format("%.2f", meterCount * pricePerMeter)}"
            }
        }

        btnAddToInventory.setOnClickListener {
            viewModel.getLoggedInCustomerId(this@MaterialDetailActivity) { customerId ->
                if (customerId != null) {
                    val wishlistItem = WishlistItem(
                        customerId = customerId,
                        name = name,
                        category = category,
                        imageUrl = imageUrl ?: "",
                        pricePerMeter = pricePerMeter
                    )

                    viewModel.addToWishlist(wishlistItem)
                    Toast.makeText(this, "$name added to your wishlist", Toast.LENGTH_SHORT).show()
                    btnAddToInventory.setImageResource(R.drawable.ic_heart) // if you have one
                } else {
                    Toast.makeText(this, "Unable to get user ID", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Add to cart
        btnAddToCart.setOnClickListener {
            Toast.makeText(this, "$meterCount meter(s) of $name added to cart", Toast.LENGTH_SHORT).show()

            viewModel.getLoggedInCustomerId(this@MaterialDetailActivity) { customerId ->
                if (customerId != null) {
                    val cartItem = CartItem(
                        name = name,
                        customerId = customerId,
                        category = category,
                        imageUrl = imageUrl ?: "",
                        pricePerMeter = pricePerMeter,
                        meters = meterCount
                    )
                    viewModel.addToCart(cartItem)
                    Toast.makeText(this, "$meterCount meter(s) of $name added to cart", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Unable to get user ID", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
