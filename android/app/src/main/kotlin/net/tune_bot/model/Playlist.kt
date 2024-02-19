package net.tune_bot.model

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.tune_bot.controller.Api
import java.net.HttpURLConnection
import java.util.UUID

data class Playlist(
    val id: UUID? = null,
    var name: String,
    var enabled: Boolean = true,
    val songs: MutableList<Song> = mutableListOf()
): AbstractModel() {
    suspend fun update(api: Api) {
        if(id == null) {
            println("can't update null playlist")
            return
        }

        val response = api.sendRequest(
            Api.Method.PATCH,
            "playlist/update",
            copy(songs = mutableListOf()).serialize()
        )

        if (response.code == HttpURLConnection.HTTP_OK) {
            val responsePlaylist: Playlist = deserialize(response.body)

            if(responsePlaylist.id == null) {
                println("id is null") // TODO display error to user
            } else {
                name = responsePlaylist.name
                enabled = responsePlaylist.enabled
            }
        } else {
            println("Http code: ${response.code}") // TODO display error to user
        }
    }

    suspend fun delete(api: Api) {
        if(id == null) {
            println("can't delete null playlist")
            return
        }

        val response = api.sendRequest(
            Api.Method.DELETE,
            "playlist/delete",
            JsonObject().apply {
                add("id", JsonPrimitive(id.toString()))
            }.toString()
        )

        if (response.code != HttpURLConnection.HTTP_OK) {
            println("Http code: ${response.code}") // TODO display error to user
        }
    }

    suspend fun addSong(api: Api, song: Song) {
        if(id == null || songs.contains(song)) {
            println("can't add song to null playlist")
            return
        }

        val response = api.sendRequest(
            Api.Method.PUT,
            "playlist/song/add",
            Gson().fromJson(song.serialize(), JsonObject::class.java).apply {
                add("playlistId", JsonPrimitive(id.toString()))
            }.toString()
        )

        if (response.code == HttpURLConnection.HTTP_OK) {
            val responseSong: Song = deserialize(response.body)

            if(responseSong.id == null) {
                println("id is null") // TODO display error to user
            } else {
                songs.add(responseSong)
            }
        } else {
            println("Http code: ${response.code}") // TODO display error to user
        }
    }

    suspend fun removeSong(api: Api, song: Song) {
        if(song.id == null || id == null || !songs.contains(song)) {
            println("can't remove null song or from null playlist")
            return
        }

        val response = api.sendRequest(
            Api.Method.DELETE,
            "playlist/song/remove",
            JsonObject().apply {
                add("playlistId", JsonPrimitive(id.toString()))
                add("songId", JsonPrimitive(song.id.toString()))
            }.toString()
        )

        if (response.code == HttpURLConnection.HTTP_OK) {
            songs.remove(song)
        } else {
            println("Http code: ${response.code}") // TODO display error to user
        }
    }

    companion object {
        suspend fun create(api: Api, userId: UUID, playlist: Playlist): Playlist? {
            if(playlist.id != null) {
                println("can't create preexisting playlist")
                return playlist
            }

            val response = api.sendRequest(
                Api.Method.POST,
                "playlist/create",
                JsonObject().apply {
                    add("name", JsonPrimitive(playlist.name))
                    add("enabled", JsonPrimitive(playlist.enabled))
                    add("userId", JsonPrimitive(userId.toString()))
                }.toString()
            )

            if (response.code == HttpURLConnection.HTTP_OK) {
                val responsePlaylist: Playlist = deserialize(response.body)

                if(responsePlaylist.id == null) {
                    println("id is null") // TODO display error to user
                } else {
                    return responsePlaylist
                }
            } else {
                println("Http code: ${response.code}") // TODO display error to user
            }

            return null
        }
    }
}