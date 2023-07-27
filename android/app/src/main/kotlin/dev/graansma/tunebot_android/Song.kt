package dev.graansma.tunebot_android

import android.media.MediaMetadataRetriever
import android.os.Build
import android.util.Log

data class Song(val url: String = "") {
    private val metaData = MediaMetadataRetriever()

    val title = metaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: ""
    val album = metaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: ""
    val artist = metaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: ""
    val duration = metaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: ""
    val image = metaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR) ?: ""
    val year = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) metaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_PRIMARY) ?: "" else ""

    init {
        try {
            if(url.isNotEmpty() && url.isNotBlank()) {
                metaData.setDataSource(url)
            }
        } catch(e: Error) {
            Log.e("init-song", e.message ?: "failed to retrieve meta data for $url")
        }
    }

    override fun equals(other: Any?) = when(other) {
        is Song -> other.url == url
        is String -> other == url
        else -> super.equals(other)
    }

    override fun toString() = "$title - $artist ($year) : $album"

    override fun hashCode() = url.hashCode()
}