package com.winesync

import java.io.File

class WineSync(private val vivinoProperties: VivinoProperties) {

    fun read(airtableBaseId: String) {
        val cli = CLI()
        val airtable = AirtableWineService(airtableBaseId)
        val vivinoWebScraper = VivinoWebScraper(vivinoProperties)

        vivinoWebScraper.download(cli)
        val winesFromVivino = VivinoCsvReader(vivinoProperties.cacheFile).read()
        val winesFromAirtable = airtable.getWines()

        val newWines = winesFromVivino.wines.filter { !winesFromAirtable.contains(it) }
        if (newWines.isNotEmpty()) {
            cli.prompt(
                    message = "Found ${newWines.size} new wines: \n${newWines.map { it.displayName() }.joinToString("\n")}.\n\n" +
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
                    message = "Found ${drunkWines.size} in Airtable that are missing in Vivino: \n${drunkWines.map { it.displayName() }.joinToString("\n")}",
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

fun main(args: Array<String>) {
    WineSync(VivinoProperties("lillesand@gmail.com", "1235453")).read("appE2hzOYu6ksFXAw")
}

