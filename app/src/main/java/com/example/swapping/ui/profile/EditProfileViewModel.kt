package com.example.swapping.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.User

class EditProfileViewModel : ViewModel()  {

    fun checkEmail(userPrev: User, user: User) : User{
        if(user.email.isBlank())
            user.email = userPrev.email
        return user
    }

    fun checkUsername(userPrev: User, user: User) : User{
        if(user.username.isNullOrBlank())
            user.username = userPrev.username
        return user
    }

    fun checkPassword(userPrev: User, user: User) : User{
        if(user.password.isNullOrBlank())
            user.password = userPrev.password
        return user
    }

    fun getUser(ID: Int, context: Context) : User {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getUserById(ID)
    }

    fun update(user: User, context: Context){
        val dbHelper = DataBaseHelper(context)
        dbHelper.updateUser(user)
    }
}