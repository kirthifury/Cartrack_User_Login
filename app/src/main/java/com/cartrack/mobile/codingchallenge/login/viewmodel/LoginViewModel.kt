package com.cartrack.mobile.codingchallenge.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cartrack.mobile.codingchallenge.R
import com.cartrack.mobile.codingchallenge.login.model.FetchResult
import com.cartrack.mobile.codingchallenge.login.model.LoggedInUser
import com.cartrack.mobile.codingchallenge.login.repository.LoginRepository
import com.cartrack.mobile.codingchallenge.login.repository.LoginResult
import com.cartrack.mobile.codingchallenge.login.ui.LoginFormState
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository): ViewModel() {
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.isNotBlank()
    }
    fun login(username: String, password: String, country: String) {
        viewModelScope.launch {

            val result = loginRepository.login(username, password, country)

            if (result is FetchResult.Success<*>) {
                _loginResult.value =
                    LoginResult(success = LoggedInUser(displayName = (result.data as LoggedInUser).displayName))
            } else {
                _loginResult.value = LoginResult(failure = (result as FetchResult.Failure).data.toString())
            }
        }
    }
}