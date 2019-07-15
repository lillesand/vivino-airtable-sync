package com.winesync

class WineSync {

    fun read(fileName: String, vivinoBaseId: String) {
        val winesFromVivino = VivinoCsvReader(fileName).read()
        val winesFromAirtable = AirtableWineService(vivinoBaseId).getWines()

        val newWines = winesFromVivino.wines.filter { !winesFromAirtable.contains(it) }

        println("Found ${newWines.size} new wines: \n${newWines.map { it.displayName() }.joinToString("\n")}")
        println("\nWould you like to add them to Airtable? (Y/n)")
        val confirmation = readLine()

        if (listOf("", "Y", "y").contains(confirmation)) {
            println ("kjør på")
        } else {
            println("Ok, skipping.")
        }

    }


}

fun main(args: Array<String>) {
    WineSync().read("./lillesand-cellar.csv", "appE2hzOYu6ksFXAw")
}

