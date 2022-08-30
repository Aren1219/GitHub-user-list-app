package com.example.githubusers.repository

import androidx.lifecycle.LiveData
import com.example.githubusers.models.list.Users
import com.example.githubusers.room.UsersEntity
import retrofit2.Response

interface Repository {

    suspend fun getRemoteData(user: String = "", followers:Boolean = true): Response<Users>

    fun getLocalData(): LiveData<UsersEntity>

    suspend fun insertDataToDb(usersEntity: UsersEntity)

    suspend fun checkIfLocalExists(): Boolean
}