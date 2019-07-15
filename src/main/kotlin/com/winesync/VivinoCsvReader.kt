package com.winesync

import org.supercsv.cellprocessor.Optional
import org.supercsv.cellprocessor.constraint.NotNull
import org.supercsv.io.CsvMapReader
import org.supercsv.prefs.CsvPreference
import java.io.FileReader

class VivinoCsvReader(fileName: String) {

    private val csvReader = CsvMapReader(FileReader(fileName), CsvPreference.STANDARD_PREFERENCE)

    fun read(): WinesFromVivino {
        val headers = arrayOf("Winery", "Wine name", "vintage", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
        val cellProcessors = arrayOf(NotNull(), NotNull(), Optional(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)

        csvReader.getHeader(true) // Skip headers
        val wines = ArrayList<VivinoWine>()
        var row = csvReader.read(headers, cellProcessors)
        while (row != null) {
            wines.add(VivinoWine(row["Winery"] as String, row["Wine name"] as String, row["vintage"] as? String))
            row = csvReader.read(headers, cellProcessors)
        }

        return WinesFromVivino(wines)
    }

}

data class VivinoWine(override val winery: String, override val name: String, override val vintage: String?) : Wine

data class WinesFromVivino(val wines: List<VivinoWine>)
