package com.example.databinding.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.databinding.dao.UserDao
import com.example.databinding.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [User::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    private class UserDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var userDao: UserDao = database.userDao()

                    userDao.deleteAll()

                    var user = User(1, "IVan", "Cousillas", 75)
                    userDao.insert(user)
                    user = User(2, "Lucille", "Cousillas", 777)
                    userDao.insert(user)
                }
            }
        }
    }

    companion object {
        // Singleton to prevent multiple instances of the database
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "learningRoom.db"
                ).addCallback(UserDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}