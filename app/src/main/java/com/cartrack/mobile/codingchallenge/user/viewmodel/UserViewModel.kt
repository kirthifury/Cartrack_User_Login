package com.cartrack.mobile.codingchallenge.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cartrack.mobile.codingchallenge.user.model.usersItem
import com.cartrack.mobile.codingchallenge.user.repository.UserRepository

class UserViewModel(private val repository: UserRepository): ViewModel() {
    private val _userList = MutableLiveData<MutableList<usersItem>>()
    val userListState: LiveData<MutableList<usersItem>> = _userList

    fun getUsers() {
        _userList.value = repository.getUsers()
    }
}