package com.example.githubusers.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubusers.models.Users

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(usersEntity: UsersEntity)

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun readUsers(): LiveData<UsersEntity>

    @Query("SELECT * FROM users")
    suspend fun getUsers(): UsersEntity
}