package com.example.Lab8.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Import các màn hình từ đúng thư mục
import com.example.Lab8.view.auth.LoginScreen
import com.example.Lab8.view.auth.RegisterScreen
import com.example.Lab8.viewmodel.AuthViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    // Khởi tạo cỗ máy xử lý dữ liệu Sản phẩm

    NavHost(navController = navController, startDestination = "login") {

        // 1. Màn hình Đăng nhập
        composable("login") {
            LoginScreen(navController, authViewModel)
        }

        // 2. Màn hình Đăng ký
        composable("register") {
            RegisterScreen(navController, authViewModel)
        }

        composable("user_home") {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Đây là trang user")
            }
        }

// 4. Màn hình Admin (Danh sách)
        composable("admin_home") {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Đây là trang admin")
            }
        }

    }
}