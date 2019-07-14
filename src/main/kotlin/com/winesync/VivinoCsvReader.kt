package com.winesync

import org.supercsv.cellprocessor.Optional
import org.supercsv.cellprocessor.constraint.NotNull
import org.supercsv.io.CsvMapReader
import org.supercsv.prefs.CsvPreference
import java.io.FileReader

class VivinoCsvReader(fileName: String) {

    private val csvReader = CsvMapReader(FileReader(fileName), CsvPreference.STANDARD_PREFERENCE)

    fun read(): List<Wine> {
        val headers = arrayOf("Winery", "Wine name", "Vintage", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
        val cellProcessors = arrayOf(NotNull(), NotNull(), Optional(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)

        csvReader.getHeader(true) // Skip headers
        val wines = ArrayList<Wine>()
        var row = csvReader.read(headers, cellProcessors)
        while (row != null) {
            wines.add(Wine(row["Winery"] as String, row["Wine name"] as String, row["Vintage"] as? String))
            row = csvReader.read(headers, cellProcessors)
        }

        return wines
    }

}

data class Wine(val winery: String, val wineName: String, val Vintage: String?)
