package com.winesync

class WineSync {

    fun read(fileName: String, vivinoBaseId: String) {
        val winesFromVivino = VivinoCsvReader(fileName).read()
        val winesFromAirtable = AirtableWineService(vivinoBaseId).getWines()

        val newWines = winesFromVivino.wines.filter { !winesFromAirtable.contains(it) }

        val cli = CLI()

        cli.prompt(
                message = "Found ${newWines.size} new wines: \n${newWines.map { it.displayName() }.joinToString("\n")}",
                question = "Would you like to add them to Airtable? (Y/n)",
                onConfirmation = {},
                onRejection = { println("Ok, skipping." )}
        )
    }


}

fun main(args: Array<String>) {
    WineSync().read("./lillesand-cellar.csv", "appE2hzOYu6ksFXAw")
}

