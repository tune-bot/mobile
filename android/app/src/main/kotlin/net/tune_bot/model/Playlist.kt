package net.tune_bot.model

import java.io.Serializable
import java.util.UUID

data class Playlist(
    val id: UUID? = null,
    val name: String,
    val enabled: Boolean = true,
    val songs: List<Song> = emptyList()
): Serializable