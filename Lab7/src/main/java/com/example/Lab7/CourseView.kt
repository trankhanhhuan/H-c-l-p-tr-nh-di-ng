package com.example.Lab7.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.Lab7.viewmodel.CourseViewModel

@Composable
fun AddCourseScreen(navController: NavController, viewModel: CourseViewModel) {
    val context = LocalContext.current
    var courseName by remember { mutableStateOf("") }
    var courseDuration by remember { mutableStateOf("") }
    var courseDescription by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = courseName, onValueChange = { courseName = it },
            label = { Text("Tên Khóa Học") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = courseDuration, onValueChange = { courseDuration = it },
            label = { Text("Thời lượng khóa học (tháng) ") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = courseDescription, onValueChange = { courseDescription = it },
            label = { Text("Mô tả khóa học") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (courseName.isEmpty() || courseDuration.isEmpty() || courseDescription.isEmpty()) {
                    Toast.makeText(context, "Hãy điền hết các mục", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                viewModel.addCourse(courseName, courseDuration, courseDescription,
                    onSuccess = {
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
                        courseName = ""; courseDuration = ""; courseDescription = ""
                    },
                    onFailure = {
                        Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Thêm")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { navController.navigate("list_courses") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xem list khóa học")
        }
    }
}

@Composable
fun CourseListScreen(navController: NavController, viewModel: CourseViewModel) {
    val courseList by viewModel.courseList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCourses()
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(courseList) { course ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { navController.navigate("update_course/${course.courseID}") },
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Tên: ${course.courseName}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Thời lượng (tháng): ${course.courseDuration}")
                    Text(text = "Mô tả: ${course.courseDescription}")
                }
            }
        }
    }
}

@Composable
fun UpdateCourseScreen(navController: NavController, viewModel: CourseViewModel, courseId: String) {
    val context = LocalContext.current
    val courseList by viewModel.courseList.collectAsState()

    val currentCourse = courseList.find { it.courseID == courseId }

    var courseName by remember { mutableStateOf(currentCourse?.courseName ?: "") }
    var courseDuration by remember { mutableStateOf(currentCourse?.courseDuration ?: "") }
    var courseDescription by remember { mutableStateOf(currentCourse?.courseDescription ?: "") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = courseName, onValueChange = { courseName = it },
            label = { Text("Tên khóa học") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = courseDuration, onValueChange = { courseDuration = it },
            label = { Text("Thời lượng (tháng)") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = courseDescription, onValueChange = { courseDescription = it },
            label = { Text("Mô tả") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.updateCourse(courseId, courseName, courseDuration, courseDescription,
                    onSuccess = {
                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                        navController.popBackStack() //
                    },
                    onFailure = {
                        Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cập nhât")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Nút xoá
        Button(
            onClick = {
                viewModel.deleteCourse(courseId,
                    onSuccess = {
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    onFailure = {
                        Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xóa khóa học", color = Color.White)
        }
    }
}