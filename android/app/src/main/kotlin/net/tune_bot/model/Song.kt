package net.tune_bot.model

import java.util.UUID

data class Song(
    val id: UUID? = null,
    val title: String,
    val artist: String,
    val album: String,
    val year: Int
): AbstractModel() {

}