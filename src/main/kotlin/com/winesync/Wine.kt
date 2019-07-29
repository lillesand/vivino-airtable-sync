package com.winesync

interface Wine {
    fun isSame(that: Wine): Boolean {
        return this.winery == that.winery && this.name == that.name && this.vintage == that.vintage
    }

    fun displayName(): String {
        val numberOfBottlesString = if(numberOfBottles == 1) "$numberOfBottles bottle" else "$numberOfBottles bottles"

        return "$numberOfBottlesString: $winery $name $vintage"
    }

    val winery: String
    val name: String
    val vintage: String?
    val numberOfBottles: Int

}
