package com.winesync

interface Wine {
    fun isSame(that: AirtableWine): Boolean {
        return this.winery == that.winery && this.name == that.name && this.vintage == that.vintage
    }

    fun displayName(): String {
        return "$winery $name $vintage"
    }

    val winery: String
    val name: String
    val vintage: String?

}
