package com.example.githubusers.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [UsersEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class UsersDatabase: RoomDatabase() {
    abstract fun getUsersDao(): UsersDao
}