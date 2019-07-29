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
import java.util.*


class VivinoWebScraper(private val vivinoProperties: VivinoProperties) {

    private val httpClient = HttpClients.custom()
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD).build())
            .build()

    fun download(cli: CLI) {
        if (vivinoProperties.cacheFile.exists()) {
            cli.prompt("${vivinoProperties.cacheFile} exists from ${Date(vivinoProperties.cacheFile.lastModified())}.", "Do you want to overwrite? No uses the existing file.",
                    onConfirmation = {
                        login(vivinoProperties.username, vivinoProperties.getPassword())
                        downloadCellar(vivinoProperties.cacheFile)
                    }, onRejection = {})
        } else {
            File(vivinoProperties.cacheFile.parent).mkdirs()
            login(vivinoProperties.username, vivinoProperties.getPassword())
            downloadCellar(vivinoProperties.cacheFile)
        }
    }

    internal fun login(username: String, password: String) {
        val csrfToken = getCsrfToken()

        val post = HttpPost("https://www.vivino.com/login")
        post.addHeader("x-csrf-token", csrfToken)
        post.entity = StringEntity("{\"email\":\"$username\",\"password\":\"$password\"}", ContentType.APPLICATION_JSON)

        val httpResponse = httpClient.execute(post)

        if (httpResponse.statusLine.statusCode != 200) {
            throw VivinoException("Vivino login failed with code ${httpResponse.statusLine.statusCode}")
        }

        val response = httpResponse.entity.content.bufferedReader().use { it.readText() }

        if (!response.contains("\"success\":true")) {
            throw VivinoException("Vivino login failed:\n$response")
        }
    }

    internal fun downloadCellar(target: File) {
        val get = HttpGet("https://www.vivino.com/users/${vivinoProperties.userId}/export.csv?data=cellar")

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

class VivinoException(msg: String) : Throwable(msg)
