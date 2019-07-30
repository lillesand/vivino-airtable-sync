package com.winesync

interface Wines {

    val wines: List<Wine>

    fun contains(wine: Wine): Boolean {
        return find(wine) != null
    }

    fun find(wine: Wine): Wine? {
        return wines.firstOrNull { wine.isSame(it) }
    }
}
