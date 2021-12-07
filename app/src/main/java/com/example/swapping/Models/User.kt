package com.example.swapping.Models


class User(
    var ID: Int = 0,
    var username: String,
    var email: String,
    var name: String,
    var city: String = "",
    var phone_number: String,
    var logged_in: Boolean = false,
    var mean_rate: Double = 0.0,
    var password: String = ""
){
    override fun toString(): String {
        return "ID: $ID, Username: $username, Email: $email, Name: $name, City: $city, Phone: $phone_number, Logged: $logged_in, Mean rate: $mean_rate"
    }
}