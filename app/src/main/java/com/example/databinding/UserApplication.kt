package com.example.databinding

import android.app.Application
import com.example.databinding.database.AppDatabase
import com.example.databinding.repo.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class UserApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val db: AppDatabase by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository: UserRepository by lazy { UserRepository(db.userDao()) }
}