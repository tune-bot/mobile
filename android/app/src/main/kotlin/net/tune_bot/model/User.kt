package net.tune_bot.model

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import net.tune_bot.controller.Api
import java.io.File
import java.io.FileReader
import java.io.Serializable
import java.net.HttpURLConnection
import java.util.UUID

data class User(
    val id: UUID? = null,
    val username: String,
    val password: String = "",
    val blacklist: Playlist = Playlist(name = "Blacklist"),
    val playlists: List<Playlist> = emptyList()
): Serializable {
    fun save(file: File) = file.writeText(serialize())

    fun serialize() = Gson().toJson(this.copy(password = ""))

    companion object {
        fun deserialize(raw: String): User = Gson().fromJson(raw, User::class.java)

        fun load(file: File): User = JsonReader(FileReader(file)).use {
            Gson().fromJson(it, User::class.java)
        }

        suspend fun login(api: Api, username: String, password: String) =
            sendRequest(api, username, password, false)

        suspend fun register(api: Api, username: String, password: String) =
            sendRequest(api, username, password, true)

        private suspend fun sendRequest(
            api: Api,
            username: String,
            password: String,
            needsToRegister: Boolean
        ): User? {
            val response = api.sendRequest(
                Api.Method.POST,
                "user/" + if (needsToRegister) "register/" else "login/",
                "{\"username\": \"$username\",\"password\": \"$password)\"}"
            )

            if (response.code == HttpURLConnection.HTTP_OK) {
                val user = deserialize(response.body)

                if(user.id == null) {
                    println("id is null") // TODO display error to user
                } else {
                    return user
                }
            } else {
                println("Http code: ${response.code}") // TODO display error to user
            }

            return null
        }
    }
}