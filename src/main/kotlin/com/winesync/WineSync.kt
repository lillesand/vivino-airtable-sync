package com.winesync

class WineSync {

    fun read(fileName: String, vivinoBaseId: String) {
        val airtable = AirtableWineService(vivinoBaseId)

        val winesFromVivino = VivinoCsvReader(fileName).read()
        val winesFromAirtable = airtable.getWines()

        val cli = CLI()

        val newWines = winesFromVivino.wines.filter { !winesFromAirtable.contains(it) }
        if (newWines.isEmpty()) {
            println("Airtable is up to sync with Vivino 😎")
        } else {
            cli.prompt(
                    message = "Found ${newWines.size} new wines: \n${newWines.map { it.displayName() }.joinToString("\n")}",
                    question = "Would you like to add them to Airtable? (Y/n)",
                    onRejection = { println("Ok, skipping.") },
                    onConfirmation = {
                        val winesToSave = newWines.map { AirtableWine(it.winery, it.name, it.vintage, it.regionalWineType, it.country, it.region, it.wineType, it.rating, it.noBottles, null, null) }
                        airtable.saveNew(cli, winesToSave)
                    }
            )
        }
    }


}

fun main(args: Array<String>) {
    WineSync().read("./lillesand-cellar.csv", "appE2hzOYu6ksFXAw")
}

