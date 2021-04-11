package com.cartrack.mobile.codingchallenge.login.repository

import androidx.annotation.WorkerThread
import com.cartrack.mobile.codingchallenge.login.database.LoginDao
import com.cartrack.mobile.codingchallenge.login.model.FetchResult
import com.cartrack.mobile.codingchallenge.login.model.LoggedInUser

class LoginDataSource(private val db: LoginDao) {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun login(username: String, password: String, country: String): FetchResult<Any> {
        return try {
            val user = db.fetchUser(username)
            if (null == user) {
                FetchResult.Failure("Couldn't find your account. If you're a new user, Please do register.")
            } else if (password == user.password && country == user.country) {
                FetchResult.Success(LoggedInUser(username))
            } else if (password != user.password) {
                FetchResult.Failure("Hmm, that's not the right password. Please try again or use Forgot Password.")
            } else if (country != user.country) {
                FetchResult.Failure("User country doesn't match.")
            } else {
                FetchResult.Failure("Something went wrong. Please try again!")
            }
        } catch (e: Throwable) {
            FetchResult.Failure("Something went wrong. Please try again!")

        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}