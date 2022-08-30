package com.example.githubusers.repository

import androidx.lifecycle.LiveData
import com.example.githubusers.api.ApiDetails
import com.example.githubusers.api.ApiReferences.FOLLOWERS
import com.example.githubusers.api.ApiReferences.FOLLOWING
import com.example.githubusers.models.list.Users
import com.example.githubusers.room.UsersDao
import com.example.githubusers.room.UsersEntity
import retrofit2.Response
import javax.inject.Inject
class RepositoryImp @Inject constructor(
    val apiDetails: ApiDetails,
    val usersDao: UsersDao,
): Repository {

    override suspend fun getRemoteData(user: String, followers: Boolean): Response<Users> {
        return if (user != "") {
            if (followers) {
                apiDetails.getFollowers(user, FOLLOWERS)
            } else{
                apiDetails.getFollowers(user ,FOLLOWING)
            }
        } else {
            apiDetails.getUsers()
        }
    }

    override suspend fun insertDataToDb(usersEntity: UsersEntity) {
        usersDao.insertUsers(usersEntity)
    }

    override fun getLocalData(): LiveData<UsersEntity> = usersDao.readUsers()

    override suspend fun checkIfLocalExists(): Boolean {
        return try {
            usersDao.getUsers().users.size > 0
        } catch (e:Exception){
            false
        }
    }
}

