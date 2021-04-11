package com.cartrack.mobile.codingchallenge.login.repository

import com.cartrack.mobile.codingchallenge.login.model.LoggedInUser

data class LoginResult(
    val success: LoggedInUser? = null,
    val failure: String? = null,
    val error: Int? = null
)