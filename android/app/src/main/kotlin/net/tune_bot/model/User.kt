package net.tune_bot.model

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.tune_bot.controller.Api
import java.net.HttpURLConnection
import java.util.UUID

data class User(
    val id: UUID? = null,
    val username: String,
    val password: String = "",
    val blacklist: Playlist = Playlist(name = "Blacklist"),
    val playlists: MutableList<Playlist> = mutableListOf()
): AbstractModel() {
    override fun serialize(): String = Gson().toJson(this.copy(password = ""))

    companion object {
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
                JsonObject().apply {
                    add("username", JsonPrimitive(username))
                    add("password", JsonPrimitive(password))
                }.toString()
            )

            if (response.code == HttpURLConnection.HTTP_OK) {
                val user: User = deserialize(response.body)

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