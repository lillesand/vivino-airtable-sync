package com.winesync

data class AirtableWine(
        val id: String,
        val winery: String,
        val name: String,
        val vintage: String?,
        val wineType: String?,
        val country: String,
        val region: String?,
        val wineStyle: String?,
        val noBottles: Int,
        val noUnplacedBottles: Int?,
        val noPlacedBottles: Int?
)
