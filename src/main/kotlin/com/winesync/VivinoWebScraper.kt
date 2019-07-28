package com.winesync

import org.apache.http.client.config.RequestConfig
import org.apache.http.entity.ContentType
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class VivinoWebScraper {

    private val httpClient = HttpClients.custom()
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD).build())
            .build()


    fun login(username: String, password: String) {
        val csrfToken = getCsrfToken()

        val post = HttpPost("https://www.vivino.com/login")
        post.addHeader("x-csrf-token", csrfToken)
        post.entity = StringEntity("{\"email\":\"$username\",\"password\":\"$password\"}", ContentType.APPLICATION_JSON)

        val response = httpClient.execute(post)
        response.close()

        if (response.statusLine.statusCode != 200) {
            throw VivinoException("Vivino login failed with code ${response.statusLine.statusCode}")
        }
    }

    fun downloadCellar(target: File) {
        val get = HttpGet("https://www.vivino.com/users/1235453/export.csv?data=cellar")

        val response = httpClient.execute(get)

        if (response.statusLine.statusCode != 200) {
            throw VivinoException("Vivino cellar download failed with code ${response.statusLine.statusCode}")
        }

        response.entity.writeTo(FileOutputStream(target))
    }

    private fun getCsrfToken(): String {
        val frontPage = httpClient.execute(HttpGet("https://www.vivino.com"))

        val frontpageBytes = ByteArrayOutputStream()
        frontPage.entity.writeTo(frontpageBytes)

        val frontPageHtml = frontpageBytes.toString("utf-8")
        val csrfToken = frontPageHtml.substringAfter("<meta name=\"csrf-token\" content=\"").substringBefore("\" />")

        frontPage.close()
        return csrfToken
    }

}

fun main(args: Array<String>) {
    val vivinoWebScraper = VivinoWebScraper()
    vivinoWebScraper.login("lillesand@gmail.com", System.getenv("VIVINO_PASSWORD"))
    val cacheFile = File("./.cache/cellar.csv")
    File(cacheFile.parent).mkdirs()
    vivinoWebScraper.downloadCellar(cacheFile)
}

class VivinoException(msg: String) : Throwable(msg)
