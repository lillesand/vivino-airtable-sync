package com.winesync

import com.sybit.airtable.Airtable

class AirtableWineService(private val baseId: String) {

    private val airtable = Airtable().configure()

    fun getWines(): WinesFromAirtable {
        val winePojos = airtable.base(baseId).table("Vin", AirtableWinePojoJava::class.java).select() as List<AirtableWinePojoJava>
        val wines = winePojos.map {
            try {
                AirtableWine(it.id, it.winery, it.name, it.vintage, it.wineType, it.country, it.region, it.wineStyle, it.noBottles, it.noUnplacedBottles, it.noPlacedBottles)
            } catch (e: IllegalStateException) {
                println("Failed to parse data from Airtable, probably because of unexpeted null field. Failed on ${it.winery} ${it.name}")
                throw e
            }
        }
        return WinesFromAirtable(wines)
    }
}

data class AirtableWine (
        val id: String,
        override val winery: String,
        override val name: String,
        override val vintage: String?,
        val wineType: String?,
        val country: String,
        val region: String?,
        val wineStyle: String?,
        val noBottles: Int,
        val noUnplacedBottles: Int?,
        val noPlacedBottles: Int?
) : Wine

data class WinesFromAirtable(val wines: List<AirtableWine>) {
    fun contains(wine: Wine): Boolean {
        return wines.firstOrNull { wine.isSame(it) } != null
    }
}
