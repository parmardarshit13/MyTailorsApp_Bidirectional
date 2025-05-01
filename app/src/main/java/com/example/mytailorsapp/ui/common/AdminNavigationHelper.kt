package com.example.mytailorsapp.ui.common

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.mytailorsapp.ui.admin.AdminDashboardActivity
import com.example.mytailorsapp.ui.admin.AdminManageInventoryActivity
import com.example.mytailorsapp.ui.admin.AdminManageWorkersActivity
import com.example.mytailorsapp.ui.admin.AdminProfileActivity
import com.example.mytailorsapp.ui.admin.WorkerSearchActivity

object AdminNavigationHelper {

    fun handleNavigation(activity: Activity, option: SidebarMenuHelper.Option) {
        when (option) {
            SidebarMenuHelper.Option.ADMIN_DASHBOARD -> {
                if (activity !is AdminDashboardActivity) {
                    activity.startActivity(Intent(activity, AdminDashboardActivity::class.java))
                    activity.finish()
                }
            }

            SidebarMenuHelper.Option.ADMIN_WORKERS -> {
                if (activity !is AdminManageWorkersActivity) {
                    activity.startActivity(Intent(activity, AdminManageWorkersActivity::class.java))
                    activity.finish()
                }
            }

//            SidebarMenuHelper.Option.ADMIN_SHOPS -> {
//                if (activity !is AdminManageShopsActivity) {
//                    activity.startActivity(Intent(activity, AdminManageShopsActivity::class.java))
//                    activity.finish()
//                }
//            }

            SidebarMenuHelper.Option.ADMIN_INVENTORY -> {
                if (activity !is AdminManageInventoryActivity) {
                    activity.startActivity(Intent(activity, AdminManageInventoryActivity::class.java))
                    activity.finish()
                }
            }

            SidebarMenuHelper.Option.ADMIN_SEARCH -> {
                if (activity !is WorkerSearchActivity) {
                    activity.startActivity(Intent(activity, WorkerSearchActivity::class.java))
                    activity.finish()
                }
            }

            SidebarMenuHelper.Option.ADMIN_PROFILE -> {
                if (activity !is AdminProfileActivity) {
                    activity.startActivity(Intent(activity, AdminProfileActivity::class.java))
                    activity.finish()
                }
            }

            else -> Toast.makeText(activity, "Clicked on $option", Toast.LENGTH_SHORT).show()
        }
    }
}
