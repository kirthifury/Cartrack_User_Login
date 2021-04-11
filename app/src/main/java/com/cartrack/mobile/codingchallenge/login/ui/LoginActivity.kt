package com.cartrack.mobile.codingchallenge.login.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cartrack.mobile.codingchallenge.CartrackApplication
import com.cartrack.mobile.codingchallenge.R
import com.cartrack.mobile.codingchallenge.login.model.LoggedInUser
import com.cartrack.mobile.codingchallenge.login.utils.afterTextChanged
import com.cartrack.mobile.codingchallenge.login.viewmodel.LoginViewModel
import com.cartrack.mobile.codingchallenge.login.viewmodel.LoginViewModelFactory
import com.hbb20.CountryCodePicker

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private var errorMessageView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val countryNamePicker = findViewById<CountryCodePicker>(R.id.country_name_picker)
        errorMessageView = findViewById(R.id.error_message)
        setTextChangeListener(username, password)
        password.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString(),
                            countryNamePicker.selectedCountryName
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(
                    username.text.toString(),
                    password.text.toString(),
                    countryNamePicker.selectedCountryName
                )
            }
        }
        loginViewModel = ViewModelProvider(
                this,
        LoginViewModelFactory((application as CartrackApplication).repository)
        )
        .get(LoginViewModel::class.java)
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null || loginResult.failure != null) {
                showLoginFailed(loginResult.failure ?: "")
            } else if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                setResult(Activity.RESULT_OK)
                finish()
            }

        })
    }

    private fun updateUiWithUser(model: LoggedInUser) {

    }

    private fun showLoginFailed(errorString: String) {
        errorMessageView?.text = errorString
    }


    private fun setTextChangeListener(username: EditText, password: EditText) {
        username.afterTextChanged {
            errorMessageView?.text = ""
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                errorMessageView?.text = ""
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }
        }
    }
}