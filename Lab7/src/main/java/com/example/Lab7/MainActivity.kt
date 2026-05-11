package com.example.Lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.Lab7.ui.screens.AddCourseScreen
import com.example.Lab7.ui.screens.CourseListScreen
import com.example.Lab7.ui.screens.UpdateCourseScreen
import com.example.Lab7.ui.theme.FirebaseprojectTheme
import com.example.Lab7.viewmodel.CourseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseprojectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val courseViewModel: CourseViewModel = viewModel()

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                backgroundColor = Color(0xFF0F9D58),
                                title = {
                                    Text(
                                        text = "CRUD Basic App",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                }
                            )
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "add_course",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("add_course") {
                                AddCourseScreen(navController, courseViewModel)
                            }

                            composable("list_courses") {
                                CourseListScreen(navController, courseViewModel)
                            }
                            composable("update_course/{courseId}") { backStackEntry ->
                                val id = backStackEntry.arguments?.getString("courseId") ?: ""
                                UpdateCourseScreen(navController, courseViewModel, id)
                            }
                        }
                    }
                }
            }
        }
    }
}