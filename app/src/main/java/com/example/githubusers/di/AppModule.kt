package com.example.githubusers.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubusers.api.ApiDetails
import com.example.githubusers.api.ApiReferences.BASE_URL
import com.example.githubusers.repository.Repository
import com.example.githubusers.repository.RepositoryImp
import com.example.githubusers.room.UsersDao
import com.example.githubusers.room.UsersDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiDetails():ApiDetails {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiDetails::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UsersDatabase{
        return Room.databaseBuilder(
            context,
            UsersDatabase::class.java,
            "UsersDatabase",
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(usersDatabase: UsersDatabase):UsersDao =
        usersDatabase.getUsersDao()

    @Provides
    @Singleton
    fun provideRep(
        apiDetails: ApiDetails,
        usersDao: UsersDao
    ): Repository = RepositoryImp(apiDetails, usersDao)
}