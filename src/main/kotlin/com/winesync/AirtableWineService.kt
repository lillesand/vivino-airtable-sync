package com.winesync

import com.sybit.airtable.Airtable

class AirtableWineService {

    private val airtable = Airtable().configure()
    private val base = "appE2hzOYu6ksFXAw"

    fun getWines(): List<AirtableWine> {
        val winePojos: List<AirtableWinePojoJava> = airtable.base(base).table("Vin", AirtableWinePojoJava::class.java).select() as List<AirtableWinePojoJava>
        return winePojos.map {
            try {
                AirtableWine(it.id, it.winery, it.name, it.vintage, it.wineType, it.country, it.region, it.wineStyle, it.noBottles, it.noUnplacedBottles, it.noPlacedBottles)
            } catch (e: IllegalStateException) {
                println("Failed to parse data from Airtable, probably because of unexpeted null field. Failed on ${it.winery} ${it.name}")
                throw e
            }
        }
    }


}
