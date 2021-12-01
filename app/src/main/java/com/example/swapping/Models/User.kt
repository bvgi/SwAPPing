package com.example.swapping.Models

class User(val ID: Int = 0,
           val username: String,
           val email: String,
           val name: String,
           val city: String = "",
           val phone_number: Int,
           val password: String,
           val logged_in: Boolean = false,
           val mean_rate: Double = 0.0
){

}