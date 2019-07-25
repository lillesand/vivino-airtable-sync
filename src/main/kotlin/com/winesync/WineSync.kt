package com.winesync

class WineSync {

    fun read(fileName: String, vivinoBaseId: String) {
        val airtable = AirtableWineService(vivinoBaseId)

        val winesFromVivino = VivinoCsvReader(fileName).read()
        val winesFromAirtable = airtable.getWines()

        val cli = CLI()

        val newWines = winesFromVivino.wines.filter { !winesFromAirtable.contains(it) }
        if (newWines.isNotEmpty()) {
            cli.prompt(
                    message = "Found ${newWines.size} new wines: \n${newWines.map { it.displayName() }.joinToString("\n")}",
                    question = "Would you like to add them to Airtable? (Y/n)",
                    onRejection = { println("Ok, skipping.") },
                    onConfirmation = {
                        val winesToSave = newWines.map { AirtableWine(it.winery, it.name, it.vintage, it.regionalWineType, it.country, it.region, it.wineType, it.rating, it.noBottles, noUnplacedBottles = null, noPlacedBottles = null, id = null) }
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

fun main(args: Array<String>) {
    WineSync().read("./lillesand-cellar.csv", "appE2hzOYu6ksFXAw")
}

