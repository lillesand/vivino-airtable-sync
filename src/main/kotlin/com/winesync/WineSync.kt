package com.winesync

class WineSync {


    init {
        //val airtable = Airtable().configure()
    }

    fun read(fileName: String) {
        val winesFromVivino = VivinoCsvReader(fileName).read()
        println(winesFromVivino)
    }


}

fun main(args: Array<String>) {
    WineSync().read("./lillesand-cellar.csv")

}
