package com.example.swapping.Models

import com.example.swapping.DataBase.UserDataBaseHelper


class Announcement(
    var ID: Int = 0,
    val user: Int,
    var title: String,
    var description: String,
    var voivodeship: String,
    var city: String,
    var category: String,
    var status: String,
    var genre: String,
    var year: String,
    var negotiation: Int = 0,
    var archived: Int = 0,
    var purchaser_id: Int,
    var image: ByteArray,
    val published_date: Int
) {
}