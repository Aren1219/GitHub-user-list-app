package com.example.githubusers.api

import com.example.githubusers.api.ApiReferences.ACCEPT
import com.example.githubusers.api.ApiReferences.USERS_END_POINT
import com.example.githubusers.api.ApiReferences.TOKEN
import com.example.githubusers.models.Users
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiDetails {

    @GET(USERS_END_POINT)
    suspend fun getUsers(
        @Header("accept") accept: String = ACCEPT,
        @Header("Authorization") token: String = TOKEN,
//        @Query("per_page") perPage: Int = PER_PAGE
    ) :Response<Users>

    @GET("$USERS_END_POINT/{user}/{followersOrFollowing}")
    suspend fun getFollowers(
        @Path("user")user: String,
        @Path("followersOrFollowing") followersOrFollowing: String
    ): Response<Users>
}