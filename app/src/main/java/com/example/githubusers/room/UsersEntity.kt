package com.example.githubusers.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.githubusers.models.Users

@Entity(tableName = "Users")
data class UsersEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val users: Users,
)