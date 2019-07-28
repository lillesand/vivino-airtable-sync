package com.winesync

import com.github.kittinunf.fuel.core.HeaderValues
import com.github.kittinunf.fuel.core.requests.download
import com.github.kittinunf.fuel.httpDownload
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import java.io.File

class VivinoWebScraper {

    private val sessionCookieName = "_ruby-web_session"
    private var sessionCookieValue: String? = null

    fun login(username: String, password: String) {
        val loginResponse = "https://www.vivino.com/login"
                .httpPost(listOf("email" to username, "password" to password))
                .header("Cookie" to sessionCookie())
                .response().second

        sessionCookieValue = findCookieValue(loginResponse.header("Set-Cookie"))
    }

    fun downloadCellar(target: String) {
        val response = "https://www.vivino.com/users/1235453/export.csv?data=cellar".httpDownload()
                .fileDestination { response, request -> File(target) }
                .progress { readBytes, totalBytes -> println("${readBytes / totalBytes}/100") }
                .header("Cookie" to sessionCookie())
                .download().response().second

        println(response)
    }

    private fun sessionCookie(): String {
        val frontpageResponse = "https://www.vivino.com".httpGet().response().second

        sessionCookieValue = findCookieValue(frontpageResponse.header("Set-Cookie"))

        return cookie()
    }

    private fun findCookieValue(header: HeaderValues): String {
        return header.find { it.contains(sessionCookieName) }
                ?.substringAfter('=')
                ?.substringBefore(';')
                ?: throw VivinoException("Couldn't find session cookie $sessionCookieName in response. Login won't succeed :(")
    }

    private fun cookie(): String {
        return "$sessionCookieName=${sessionCookieValue!!}"
    }


}

fun main(args: Array<String>) {
    val vivinoWebScraper = VivinoWebScraper()
    vivinoWebScraper.login("lillesand@gmail.com", "xpXCRXasoogdiYHN.4fZ")
    vivinoWebScraper.downloadCellar("./.cache/cellar.csv")
}

class VivinoException(msg: String) : Throwable(msg)
