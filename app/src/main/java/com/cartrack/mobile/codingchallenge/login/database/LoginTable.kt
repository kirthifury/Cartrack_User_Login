package com.cartrack.mobile.codingchallenge.login.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LoginDetails")
class LoginTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id = 0

    @ColumnInfo(name = "Username")
    var username: String? = null

    @ColumnInfo(name = "Password")
    var password: String? = null

    @ColumnInfo(name = "Country")
    var country: String? = null
}