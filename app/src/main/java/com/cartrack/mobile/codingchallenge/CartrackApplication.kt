package com.cartrack.mobile.codingchallenge

import android.app.Application
import com.cartrack.mobile.codingchallenge.login.database.LoginDatabase
import com.cartrack.mobile.codingchallenge.login.repository.LoginDataSource
import com.cartrack.mobile.codingchallenge.login.repository.LoginRepository
import com.cartrack.mobile.codingchallenge.user.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CartrackApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { LoginDatabase.getDatabase(this, applicationScope) }
    val dataSource by lazy { LoginDataSource(database.loginDoa()) }
    val repository by lazy { LoginRepository(dataSource) }
    val userRepository by lazy { UserRepository(this) }

}