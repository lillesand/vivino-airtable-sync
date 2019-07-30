package com.winesync

import java.io.File

class WineSync(private val vivinoProperties: VivinoProperties, private val airtableProperties: AirtableProperties) {

    private val cli = CLI()
    private val airtable = AirtableWineService(airtableProperties)
    private val vivinoWebScraper = VivinoWebScraper(vivinoProperties)

    fun run() {
        vivinoWebScraper.download(cli)
        val winesFromVivino = VivinoCsvReader(vivinoProperties.cacheFile).read()
        val winesFromAirtable = airtable.getWines()

        val newWines = winesFromVivino.wines.filter { !winesFromAirtable.contains(it) }
        if (newWines.isNotEmpty()) {
            cli.prompt(
                    message = "Found ${newWines.size} new wines: \n${newWines.joinToString("\n") { it.displayName() }}.\n\n" +
                            "That's a whopping ${newWines.map { it.numberOfBottles }.reduce { acc, i -> acc + i  }} bottles of wine!",
                    question = "Would you like to add them to Airtable? (Y/n)",
                    onRejection = { println("Ok, skipping.") },
                    onConfirmation = {
                        val winesToSave = newWines.map { AirtableWine(it.winery, it.name, it.vintage, it.regionalWineType, it.country, it.region, it.wineType, it.rating, it.numberOfBottles, noUnplacedBottles = null, noPlacedBottles = null, id = null) }
                        airtable.saveNew(cli, winesToSave)
                    }
            )
        }

        val drunkWines = winesFromAirtable.wines.filter { !winesFromVivino.contains(it) }
        if (drunkWines.isNotEmpty()) {
            cli.prompt(
                    message = "Found ${drunkWines.size} in Airtable that are missing in Vivino: \n${drunkWines.joinToString("\n") { it.displayName() }}",
                    question = "Would you like to remove them from Airtable? (Y/n)",
                    onRejection = { println("Ok, leaving them there.") },
                    onConfirmation = {
                        airtable.remove(cli, drunkWines)
                    }
            )
        }

        if (newWines.isEmpty() && drunkWines.isEmpty()) {
            cli.displayInfo("Vivino and Airtable are in sync 😎")
        }
    }

}

data class VivinoProperties(val username: String, val userId: String, val cacheFile: File = File("./.cache/cellar.csv")) {

    fun getPassword(): String {
        return System.getenv("VIVINO_PASSWORD")
                ?: throw VivinoException("Vivino password must be set in environment variable VIVINO_PASSWORD")
    }

}

data class AirtableProperties(val baseId: String)

fun main() {
    WineSync(VivinoProperties("lillesand@gmail.com", "1235453"), AirtableProperties("appE2hzOYu6ksFXAw")).run()
}

