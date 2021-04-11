package com.cartrack.mobile.codingchallenge.user.repository

import android.content.Context
import android.util.Log
import com.cartrack.mobile.codingchallenge.user.model.users
import com.cartrack.mobile.codingchallenge.user.model.usersItem
import com.google.gson.Gson
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class UserRepository(private val context: Context) {

    private var userList: MutableList<usersItem> = ArrayList()

    private var userMap: MutableMap<Int, usersItem> = HashMap()

    init {
        loadJSONFromAsset(context)
    }

    fun getUsers(): MutableList<usersItem> {
        return userList
    }

    fun getUser(userId: String): usersItem? {
        return userMap.get(userId)
    }

    private fun loadJSONFromAsset(context: Context): String {
        val json: String?
        try {
            val inputStream = context.assets.open("users_list.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            val charset: Charset = Charsets.UTF_8
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, charset)
            val gson: Gson = Gson()
            val testParsing = gson.fromJson(json, users::class.java)
            Log.e("Test parsing", "$testParsing")
            userList.clear()
            userList.addAll(testParsing)
            for (user in testParsing) {
                userMap[user.id] = user
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }
}