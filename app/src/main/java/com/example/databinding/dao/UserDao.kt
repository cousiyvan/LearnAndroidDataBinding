package com.example.databinding.dao

import androidx.room.*
import com.example.databinding.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("select * from user")
    fun getAll(): Flow<List<User>>

    @Query("select * from user where uid in (:userIds)")
    fun getUsersFromIds(userIds: List<Long>): List<User>

    @Query("select * from user where uid = :userId")
    fun getUserFromId(userId: Long): User

    @Query("select * from user where first_name like :first and last_name like :last limit 1")
    fun findByName(first: String, last: String): User

    @Query("Delete from user where uid = :userId")
    fun deleteById(userId: Long)

    @Insert
    fun insert(user: User): Long

    @Delete
    fun delete(user: User)

    @Query("Delete from user")
    fun deleteAll()

    @Query("select max(uid) from user")
    fun getMaxId(): Int

    @Update
    fun update(user: User): Int
}