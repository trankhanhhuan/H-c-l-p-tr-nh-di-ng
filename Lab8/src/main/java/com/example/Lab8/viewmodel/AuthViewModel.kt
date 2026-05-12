package com.example.Lab8.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    var notice = mutableStateOf("")
    var isRegisterSuccess = mutableStateOf(false)
    var role = mutableStateOf("")

    fun register(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) { // kiểm tra Chuỗi chỉ toàn khoảng trắng: Chỉ chứa dấu cách, tab, hoặc xuống dòng ("  ", "\t", "\n").
            notice.value = "Vui lòng nhập đầy đủ Email và Mật khẩu!"
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
                        if (verifyTask.isSuccessful) {

                            val userData = hashMapOf("email" to email, "role" to "user")

                            db.collection("users").document(user.uid).set(userData)
                                .addOnSuccessListener {
                                    // Lưu lên bảng users thành công thì mới bắt đầu đoạn này ( nhờ addOnSuccessListener)
                                    notice.value = "Đăng ký xong! Vui lòng xác nhận tại gmail."
                                    isRegisterSuccess.value = true
                                    auth.signOut()
                                }
                                .addOnFailureListener { e ->
                                    notice.value = "Lỗi: ${e.message}"
                                }

                        } else {
                            notice.value = "Lỗi: ${verifyTask.exception?.message}"
                        }
                    }
                } else {
                    notice.value = "Lỗi: ${task.exception?.message}"
                }
            }
    }

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            notice.value = "Vui lòng nhập đủ Email và Mật khẩu!"
            return
        }

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Đăng nhập thành công, bắt đầu ép Firebase tải lại dữ liệu mới nhất
                auth.currentUser?.reload()?.addOnCompleteListener { reloadTask ->
                    if (reloadTask.isSuccessful) {
                        // Tải lại xong, check lại xem đã bấm link verify thật chưa
                        if (auth.currentUser?.isEmailVerified == true) {
                            // Đã xác thực thì đi lấy Role trên Firestore
                            db.collection("users").document(auth.currentUser!!.uid).get()
                                .addOnSuccessListener { document ->
                                    role.value = document.getString("role") ?: "user"
                                }
                                .addOnFailureListener {
                                    notice.value = "Lỗi máy chủ."
                                }
                        } else {
                            // Vẫn chưa xác thực thật
                            notice.value = "Chưa xác thực email, hãy nhấp vào link xác thực trong thư spam"
                            auth.signOut()
                        }
                    } else {
                        notice.value = "Lỗi cập nhật trạng thái mạng: ${reloadTask.exception?.message}"
                    }
                }
            } else {
                notice.value = "Sai tài khoản hoặc mật khẩu !"
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    // Kiểm tra xem user này đã có trong bảng users trên Firestore chưa
                    db.collection("users").document(user.uid).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                // Đã có tài khoản (đăng nhập những lần sau) -> Lấy quyền và cho vào
                                role.value = document.getString("role") ?: "user"
                            } else {
                                // Lần đầu tiên đăng nhập bằng Google -> Tạo mới data trên Firestore
                                val userData = hashMapOf("email" to user.email, "role" to "user")
                                db.collection("users").document(user.uid).set(userData)
                                    .addOnSuccessListener {
                                        role.value = "user" // Tạo xong thì cho vào luôn
                                    }
                                    .addOnFailureListener {
                                        notice.value = "Tạo data Google thất bại!"
                                    }
                            }
                        }
                        .addOnFailureListener {
                            notice.value = "Lỗi máy chủ khi check quyền Google."
                        }
                }
            } else {
                notice.value = "Đăng nhập Google thất bại: ${task.exception?.message}"
            }
        }
    }

    // --- RESET TRẠNG THÁI ---
    fun resetState() {
        isRegisterSuccess.value = false
        notice.value = ""
        role.value = ""
    }

    // --- CHỨC NĂNG ĐĂNG XUẤT ---
    fun logout() {
        auth.signOut()
        resetState()
    }
}