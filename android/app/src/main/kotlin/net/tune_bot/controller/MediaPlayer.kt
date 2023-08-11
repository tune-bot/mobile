package net.tune_bot.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import net.tune_bot.model.Song

class MediaPlayer {
    private val queue = mutableStateListOf( // TODO actual playlists
        Song(null, "Title", "Artist", "Album", 2023),
        Song(null, "Test", "Test", "Test", 7357)
    )
    private var currentlyPlayingIndex by mutableStateOf(0)
    var isPlaying by mutableStateOf(false)
        private set

    fun play() {
        isPlaying = true
    }

    fun pause() {
        isPlaying = false
    }

    fun togglePlaying() =
        if(isPlaying) pause() else play()

    fun previous() {
        currentlyPlayingIndex--
        if(currentlyPlayingIndex < 0) {
            currentlyPlayingIndex = queue.size - 1
        }
    }

    fun current(): Song? =
        if(currentlyPlayingIndex >= 0 && currentlyPlayingIndex < queue.size) {
            queue[currentlyPlayingIndex]
        } else {
            null
        }

    fun next() {
        currentlyPlayingIndex++
        if(currentlyPlayingIndex >= queue.size) {
            currentlyPlayingIndex = 0
        }
    }

    fun shuffle() {
        queue.shuffle()
        currentlyPlayingIndex = 0
    }
}