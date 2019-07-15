package com.winesync

class WineSync {

    fun read(fileName: String) {
        val winesFromVivino = VivinoCsvReader(fileName).read()
        val wines = AirtableWineService().getWines()
        println(wines)

    }


}

fun main(args: Array<String>) {
    WineSync().read("./lillesand-cellar.csv")
}

