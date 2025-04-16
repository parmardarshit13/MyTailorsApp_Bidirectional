package com.example.mytailorsapp.navigation

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mytailorsapp.ui.auth.ForgotPasswordScreenUI
import com.example.mytailorsapp.ui.auth.LoginScreenUI
import com.example.mytailorsapp.ui.auth.RegisterScreenUI

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    context: Context)
{
    composable("login_screen") { LoginScreenUI(navController, context) }
    composable("register_screen") { RegisterScreenUI(navController) }
    composable("forgot_password_screen") { ForgotPasswordScreenUI(navController) }
}
