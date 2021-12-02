package com.example.swapping.Models


class User(
    var ID: Int = 0,
    var username: String,
    var email: String,
    var name: String,
    var city: String = "",
    var phone_number: Int,
    var logged_in: Boolean = false,
    var mean_rate: Double = 0.0
){

}