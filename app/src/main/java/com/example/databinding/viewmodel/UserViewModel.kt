package com.example.databinding.viewmodel

import android.app.Application
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.lifecycle.*
import androidx.room.Room
import com.example.databinding.R
import com.example.databinding.database.AppDatabase
import com.example.databinding.models.User
import com.example.databinding.repo.UserRepository
import com.example.databinding.utils.ListUserAdapter
import com.example.databinding.utils.ObservableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.properties.Delegates

class UserViewModel(private val userRepository: UserRepository) : ObservableViewModel() {
    private val _userFirstName: MutableLiveData<String> = MutableLiveData<String>("Ivan")
    val _userLastName: MutableLiveData<String> = MutableLiveData<String>("Cousillas")
    private val _userLikes: MutableLiveData<Int> = MutableLiveData<Int>(0)

    val userFirstName: LiveData<String> = _userFirstName
    val userLastName: LiveData<String> = _userLastName
    val userLikes: LiveData<Int> = _userLikes

    val users: LiveData<List<User>> = userRepository.allUsers.asLiveData()
    var userRef: User = User(0L, "", "", 0)

    var userLastNameEditable: String
        @Bindable get() {
            return userLastName.value ?: ""
        }
        set(value) {
            _userLastName.postValue(value)
        }

    val likesString: LiveData<String> = Transformations.map(_userLikes) {
        when {
            it == 0 -> "Don't like it"
            it in 1..4 -> "Sounds good"
            else -> "Awesome"
        }
    }

    fun onLike() {
        _userLikes.value = (_userLikes.value ?: 0) + 1
        // createOrUpdateUser()
    }

    fun insert(user: User) = viewModelScope.launch(Dispatchers.IO) {
        val id = userRepository.insert(user)
        // userRef.value = User(id, user.firstName, user.lastName, user.likes)
    }

    fun update(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.update(user)
    }

    fun delete(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.delete(user)
    }

    fun deleteById(userId: Long) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.deleteById(userId)
    }

    fun getUser(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        userRef = userRepository.getUser(id)
    }

    fun onListChange(users: List<User>, listUserAdapter: ListUserAdapter) {
        users?.let {
            listUserAdapter.submitList(it)
        }
    }

    fun onSelectUser(userId: Long) = viewModelScope.launch(Dispatchers.IO) {
        userRef = userRepository.getUser(userId)
        _userFirstName.postValue(userRef.firstName)
        _userLastName.postValue(userRef.lastName)
        // userLastNameEditable = userRef.lastName
        _userLikes.postValue(userRef.likes)
    }

    fun onSave() {
        createOrUpdateUser()
    }

    fun createOrUpdateUser() {
        if (userRef.uid == 0L) {
            // Creation
            var user = User(
                0,
                userFirstName.value ?: "",
                _userLastName.value ?: "",
                userLikes.value ?: 0
            )
            insert(user)
        } else {
            userRef = User(userRef.uid, userFirstName.value ?: "", _userLastName.value ?: "", userLikes.value ?: 0)
            update(userRef)
        }
    }
}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}