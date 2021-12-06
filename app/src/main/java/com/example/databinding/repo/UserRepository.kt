package com.example.databinding.repo

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.databinding.dao.UserDao
import com.example.databinding.models.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User): Long {
        return userDao.insert(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getMaxId(): Int {
        return userDao.getMaxId()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(user: User): Int {
        return userDao.update(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteById(userID: Long) {
        userDao.deleteById(userID)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getUser(id: Long): User {
        return userDao.getUserFromId(id)
    }
}