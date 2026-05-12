package com.example.Lab10

import android.app.Application
import com.example.Lab10.data.AppContainer
import com.example.Lab10.data.AppDataContainer

class InventoryApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}