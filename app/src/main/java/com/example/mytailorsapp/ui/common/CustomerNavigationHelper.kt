package com.example.mytailorsapp.ui.common

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.mytailorsapp.ui.customer.*

object CustomerNavigationHelper {

    fun handleNavigation(activity: Activity, option: SidebarMenuHelper.Option) {
        when (option) {
            SidebarMenuHelper.Option.CUSTOMER_DASHBOARD -> {
                if (activity !is CustomerDashboardActivity) {
                    activity.startActivity(Intent(activity, CustomerDashboardActivity::class.java))
                    activity.finish()
                }
            }

            SidebarMenuHelper.Option.CUSTOMER_CATEGORIES -> {
                if (activity !is CategoryActivity) {
                    activity.startActivity(Intent(activity, CategoryActivity::class.java))
                    activity.finish()
                }
            }

            SidebarMenuHelper.Option.CUSTOMER_MATERIALS -> {
                if (activity !is MaterialActivity) {
                    activity.startActivity(Intent(activity, MaterialActivity::class.java))
                    activity.finish()
                }
            }

//            SidebarMenuHelper.Option.CUSTOMER_SHOPS -> {
//                if (activity !is ShopSearchActivity) {
//                    activity.startActivity(Intent(activity, ShopSearchActivity::class.java))
//                    activity.finish()
//                }
//            }

            SidebarMenuHelper.Option.CUSTOMER_CART -> {
                if (activity !is CustomerCartActivity) {
                    activity.startActivity(Intent(activity, CustomerCartActivity::class.java))
                    activity.finish()
                }
            }

            SidebarMenuHelper.Option.CUSTOMER_WISHLIST -> {
                if (activity !is MyWishlistActivity) {
                    activity.startActivity(Intent(activity, MyWishlistActivity::class.java))
                    activity.finish()
                }
            }

            SidebarMenuHelper.Option.CUSTOMER_PROFILE -> {
                if (activity !is CustomerProfileActivity) {
                    activity.startActivity(Intent(activity, CustomerProfileActivity::class.java))
                    activity.finish()
                }
            }

            else -> Toast.makeText(activity, "Clicked on $option", Toast.LENGTH_SHORT).show()
        }
    }
}
