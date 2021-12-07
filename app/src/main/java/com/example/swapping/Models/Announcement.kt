package com.example.swapping.Models


class Announcement(
    var ID: Int = 0,
    val user: Int,
    var title: String,
    var description: String,
    var voivodeship: String,
    var city: String,
    var category: String,
    var status: String,
    var negotiation: Int = 0,
    var archived: Int = 0,
    var purchaser_id: Int = 0,
    var image: ByteArray,
    val published_date: Int
) {
    override fun toString(): String {
        return "ID: $ID, user: $user, title: $title, description: $description, voivode: $voivodeship, city: $city, category: $category, status: $status, negotiation: $negotiation, archived: $archived, purchaser: $purchaser_id, date: $published_date"
    }
}