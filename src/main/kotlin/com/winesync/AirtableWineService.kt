package com.winesync

import com.sybit.airtable.Airtable

class AirtableWineService(airtableProperties: AirtableProperties) {

    private val airtable = Airtable().configure()
    private val vinTable = airtable.base(airtableProperties.baseId).table("Vin", AirtableWinePojo::class.java)

    fun getWines(): WinesFromAirtable {
        @Suppress("UNCHECKED_CAST")
        val winePojos = vinTable.select() as List<AirtableWinePojo>
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
            val pojo = AirtableWinePojo(it.winery, it.name, it.vintage, it.country, it.region, it.wineStyle, it.wineType, it.numberOfBottles, it.averageRating)
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

    fun updateAmounts(cli: CLI, changedAmount: List<AmountUpdate>) {
        changedAmount
                .forEach {
                    cli.displayInfo("Updating ${it.displayName()}")
                    vinTable.update(AirtableUpdateAmountPojo(it.airtableId, it.newAmount))
                }
    }
}

interface AmountUpdate {
    val airtableId: String
    val newAmount: Int

    fun displayName(): String
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
