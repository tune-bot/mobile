package dev.graansma.tunebot_android

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class Api(promotionLevel: PromotionLevel) {
    private val host: String = when(promotionLevel) {
        PromotionLevel.DEVELOPMENT -> "localhost"
        PromotionLevel.PRODUCTION -> "tunebot-api.graansma.dev"
    }

    fun getPlaylist(mac: String): Playlist {
        val songs = mutableSetOf<String>()
        val blacklist = mutableSetOf<String>()
        val response = JSONObject(sendGetRequest(mac))
        var array = response.optJSONArray("playlists") ?: JSONArray()

        for(i in 0 until array.length()) {
            val songArray = array.optJSONObject(i).optJSONArray("songs") ?: JSONArray()
            if(array.optJSONObject(i).optBoolean("enabled")) {
                for (j in 0 until songArray.length()) {
                    val song = songArray.optJSONObject(j)?.optString("url")
                    if (song != null) songs.add(song)
                }
            }
        }

        array = response.optJSONObject("blacklist")?.optJSONArray("songs") ?: JSONArray()
        for(i in 0 until array.length()) {
            val song = array.optJSONObject(i)?.optString("url")
            if(song != null) blacklist.add(song)
        }

        return Playlist(songs, blacklist)
    }

    private fun sendGetRequest(mac: String): String {
        val response = StringBuffer()

        with(URL("http://$host:8080/device/user/get/").openConnection() as HttpURLConnection) {
            val wr = OutputStreamWriter(outputStream)
            wr.write("{\"mac\":\"$mac\"}")
            wr.flush()

            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while(inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
            }
        }

        return response.toString()
    }
}