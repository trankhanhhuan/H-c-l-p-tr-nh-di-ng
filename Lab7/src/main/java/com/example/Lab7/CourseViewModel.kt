package com.example.Lab7.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.Lab7.model.Course
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class CourseViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val dbCourses = db.collection("Courses")

    private val _courseList = MutableStateFlow<List<Course>>(emptyList())
    val courseList: StateFlow<List<Course>> = _courseList

    // create
    fun addCourse(
        courseName: String, courseDuration: String, courseDescription: String,
        onSuccess: () -> Unit, onFailure: (Exception) -> Unit
    ) {
        val courseID = UUID.randomUUID().toString()
        val course = Course(courseID, courseName, courseDuration, courseDescription)

        dbCourses.document(courseID).set(course)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // get list
    fun getCourses() {
        dbCourses.get().addOnSuccessListener { queryDocumentSnapshots ->
            if (!queryDocumentSnapshots.isEmpty) {
                val list = queryDocumentSnapshots.documents.mapNotNull { doc ->
                    val course = doc.toObject(Course::class.java)
                    course?.courseID = doc.id
                    course
                }
                _courseList.value = list
            } else {
                _courseList.value = emptyList()
            }
        }.addOnFailureListener {
            Log.e("CourseViewModel", "Error getting documents: $it")
        }
    }

    // Cập nhật
    fun updateCourse(
        courseID: String, name: String, duration: String, description: String,
        onSuccess: () -> Unit, onFailure: (Exception) -> Unit
    ) {
        val updatedCourse = Course(courseID, name, duration, description)

        dbCourses.document(courseID).set(updatedCourse)
            .addOnSuccessListener {
                getCourses()
                onSuccess()
            }
            .addOnFailureListener { onFailure(it) }
    }

    // Xóa
    fun deleteCourse(courseID: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        dbCourses.document(courseID).delete()
            .addOnSuccessListener {
                getCourses()
                onSuccess()
            }
            .addOnFailureListener { onFailure(it) }
    }
}