package net.tune_bot.view

import android.widget.ExpandableListView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import net.tune_bot.controller.Api
import net.tune_bot.controller.MediaPlayer
import net.tune_bot.model.Playlist
import net.tune_bot.model.User
import net.tune_bot.view.component.CollapsableLazyColumn
import net.tune_bot.view.component.CollapsableLazyList

class PlaylistView(
    mediaPlayer: MediaPlayer,
    api: Api,
    navController: NavController,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    private val user: User?
): AbstractView(
    name = "playlist",
    icon = Icons.Default.List,
    text = "Playlist",
    navController, scaffoldState
) {
    private fun Playlist.toCollapsableLazyColumn() = object: CollapsableLazyList {
        @Composable
        override fun Header() = Row {
            Checkbox(
                checked = enabled,
                onCheckedChange = { enabled = it }
            ) // todo send api request
            Text(name)
        }
        override val list: List<@Composable () -> Unit> = songs.map {
            // TODO options to interact with media player and remove song from playlist
            { Text("${it.title} | ${it.artist} | ${it.album} | ${it.year}") }
        }
    }

    @Composable
    override fun Frame() {
        CollapsableLazyColumn(
            sections = user?.playlists?.map { it.toCollapsableLazyColumn() } ?: emptyList()
        ) // TODO option to add new playlist and add songs

        Divider()

        CollapsableLazyColumn(
            sections = user?.blacklist?.let { listOf(it.toCollapsableLazyColumn()) } ?: emptyList()
        )
    }
}