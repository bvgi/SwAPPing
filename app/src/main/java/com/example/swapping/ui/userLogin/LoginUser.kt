package com.example.swapping.ui.userLogin

public class LoginUser {
    private lateinit var emailAddress: String
    private lateinit var password: String

    public fun LoginUser(email: String, password: String){
        this.emailAddress = email
        this.password = password
    }

    public fun getEmailAddress(): String {
        return this.emailAddress
    }

    public fun getPassword(): String {
        return this.password
    }
}