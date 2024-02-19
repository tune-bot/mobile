package net.tune_bot.controller

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.tune_bot.build.PromotionLevel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class Api(promotionLevel: PromotionLevel) {
    private val host: String = when(promotionLevel) {
        PromotionLevel.DEVELOPMENT -> "localhost"
        PromotionLevel.STAGING -> "192.168.50.128"
        PromotionLevel.PRODUCTION -> "api.tune-bot.net"
    }

    suspend fun sendRequest(
        method: Method,
        path: String,
        payload: String
    ) = withContext(Dispatchers.IO) {
        val body = StringBuffer()
        var code = HttpURLConnection.HTTP_UNAVAILABLE

        try {
            println(host)
            with(URL("http://$host/" + path.removePrefix("/")).openConnection() as HttpURLConnection) {
                this.requestMethod = method.name

                OutputStreamWriter(outputStream).use {
                    it.write(payload)
                    it.flush()
                }

                code = this.responseCode

                if(code in 200..299) {
                    BufferedReader(InputStreamReader(inputStream)).use {
                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            body.append(inputLine)
                            inputLine = it.readLine()
                        }
                    }
                } else {
                    println(responseMessage)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace() // TODO do I really want to print the stack trace?
        }

        Response(code, body.toString())
    }

    data class Response(
        val code: Int,
        val body: String
    )

    enum class Method {
        GET,
        POST,
        PUT,
        PATCH,
        LINK,
        DELETE
    }
}