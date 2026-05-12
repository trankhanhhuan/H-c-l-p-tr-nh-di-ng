package com.example.Lab8.view.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.Lab8.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    // Khai báo các biến State
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val notice by viewModel.notice
    val isRegisterSuccess by viewModel.isRegisterSuccess

    // Thêm biến role để lắng nghe xem đăng ký Google xong thì nhảy trang
    val role = viewModel.role.value
    val context = LocalContext.current



    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                // Gọi hàm xử lý Google (nó sẽ tự hiểu là tạo tài khoản mới nếu chưa có)
                viewModel.loginWithGoogle(idToken)
            }
        } catch (e: ApiException) {
            viewModel.notice.value = "Lỗi khi chọn tài khoản Google!"
        }
    }

    // --- LẮNG NGHE NHẢY TRANG ---
    // Dành cho Đăng ký thường (Email/Pass)
    LaunchedEffect(isRegisterSuccess) {
        if (isRegisterSuccess) {
            delay(2500)
            email = ""
            password = ""
            confirmPassword = ""
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    // Dành cho Đăng ký Google (Vì Google đăng ký xong là vô app luôn, không cần check mail)
    LaunchedEffect(role) {
        if (role == "admin") {
            navController.navigate("admin_home") { popUpTo("register") { inclusive = true } }
        } else if (role == "user") {
            navController.navigate("user_home") { popUpTo("register") { inclusive = true } }
        }
    }

    // --- GIAO DIỆN ---
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("ĐĂNG KÝ TÀI KHOẢN", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(32.dp))

        AuthTextField(value = email, onValueChange = { email = it }, label = "Địa chỉ Email")
        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(value = password, onValueChange = { password = it }, label = "Mật khẩu", isPassword = true)
        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Xác nhận lại mật khẩu", isPassword = true)

        if (notice.isNotEmpty()) {
            Text(
                text = notice,
                color = if (isRegisterSuccess) Color(0xFF388E3C) else Color.Red,
                modifier = Modifier.padding(vertical = 12.dp),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Nút Đăng ký bằng Email/Pass
        AuthButton(
            text = "ĐĂNG KÝ NGAY",
            onClick = {
                when {
                    email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                        viewModel.notice.value = "Ní ơi, nhập đủ 3 ô giúp mình với!"
                    }
                    password != confirmPassword -> {
                        viewModel.notice.value = "Mật khẩu không khớp rồi ní!"
                    }
                    password.length < 6 -> {
                        viewModel.notice.value = "Mật khẩu phải từ 6 ký tự trở lên."
                    }
                    else -> {
                        viewModel.register(email, password)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "HOẶC", fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        // Nút Đăng ký nhanh bằng Google
//        AuthButton(
//            text = "Đăng ký nhanh bằng Google",
//            onClick = {
//                googleSignInClient.signOut().addOnCompleteListener {
//                    launcher.launch(googleSignInClient.signInIntent)
//                }
//            },
//            containerColor = Color(0xFFDB4437)
//        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("login") }) {
            Text("Đã có tài khoản? Đăng nhập ngay")
        }
    }
}