package com.winesync

interface Wines {

    val wines: List<Wine>

    fun contains(wine: Wine): Boolean {
        return wines.firstOrNull { wine.isSame(it) } != null
    }
}
