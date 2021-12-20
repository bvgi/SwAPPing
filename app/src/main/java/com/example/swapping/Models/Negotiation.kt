package com.example.swapping.Models

class Negotiation (
    var ID: Int = 0,
    val adID: Int,
    var ownerID: Int,
    var purchaserID: Int,
    var type: Int,
    var offers: String
) {
    override fun toString(): String {
        return "ID: $ID, Ad: $adID, Owner: $ownerID, Purchaser: $purchaserID, Type: $type, OffersID: $offers"
    }

}