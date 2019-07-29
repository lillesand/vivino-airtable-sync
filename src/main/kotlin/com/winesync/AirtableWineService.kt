package com.winesync

import com.sybit.airtable.Airtable

class AirtableWineService(baseId: String) {

    private val airtable = Airtable().configure()
    private val vinTable = airtable.base(baseId).table("Vin", AirtableWinePojoJava::class.java)

    fun getWines(): WinesFromAirtable {
        @Suppress("UNCHECKED_CAST")
        val winePojos = vinTable.select() as List<AirtableWinePojoJava>
        val wines = winePojos.map {
            try {
                val averageRating = it.averageRating.replace(",", ".").toDouble()
                AirtableWine(it.winery, it.name, it.vintage, it.wineType, it.country, it.region, it.wineStyle, averageRating, it.noBottles, it.noUnplacedBottles, it.noPlacedBottles, it.wine, it.id)
            } catch (e: IllegalStateException) {
                println("Failed to parse data from Airtable, probably because of unexpeted null field. Failed on ${it.winery} ${it.name}")
                throw e
            }
        }
        return WinesFromAirtable(wines)
    }

    fun saveNew(cli: CLI, newWines: List<AirtableWine>) {
        val progress = cli.newProgress(newWines.size)

        newWines.forEach {
            val pojo = AirtableWinePojoJava(it.winery, it.name, it.vintage, it.country, it.region, it.wineStyle, it.wineType, it.numberOfBottles, it.averageRating)
            progress.increment("Saving ${it.displayName()}")
            vinTable.create(pojo)

            Thread.sleep(100) // Max 5 calls per second. This should definitely do.
        }
    }

    fun remove(cli: CLI, winesToRemove: List<AirtableWine>) {
        val progress = cli.newProgress(winesToRemove.size)

        winesToRemove.forEach {
            progress.increment("Deleting ${it.wine}")
            vinTable.destroy(it.id)

            Thread.sleep(100) // Max 5 calls per second. This should definitely do.
        }
    }
}

data class AirtableWine(
        override val winery: String,
        override val name: String,
        override val vintage: String?,
        val wineType: String?,
        val country: String?,
        val region: String?,
        val wineStyle: String?,
        val averageRating: Double,
        override val numberOfBottles: Int,
        val noUnplacedBottles: Int?,
        val noPlacedBottles: Int?,
        val wine: String = "$winery $name ${vintage.orEmpty()}".trim(),
        val id: String?
) : Wine

data class WinesFromAirtable(override val wines: List<AirtableWine>): Wines
