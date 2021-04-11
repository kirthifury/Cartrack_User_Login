package com.cartrack.mobile.codingchallenge.login.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LoginDao {
    @Insert
    suspend fun insertDetails(data: LoginTable?)

    @get:Query("select * from LoginDetails")
    val details: LiveData<List<LoginTable>>?

    @Query("delete from LoginDetails")
    fun deleteAllData()

    @Query("select count(1) from LoginDetails where username = :username and password = :password and country = :country")
    suspend fun fetchUser(username: String, password: String, country: String) : Int

    @Query("select id, username, password, country from LoginDetails where username = :username")
    suspend fun fetchUser(username: String): LoginTable
}