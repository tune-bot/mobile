package net.tune_bot.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.tune_bot.activity.AbstractActivity
import net.tune_bot.controller.Api
import net.tune_bot.controller.MediaPlayer
import net.tune_bot.model.Playlist
import net.tune_bot.view.component.CollapsableList
import net.tune_bot.view.shared.toCollapsableListContent

class PlaylistView(
    mediaPlayer: MediaPlayer,
    private val api: Api,
    navController: NavController,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
): AbstractView(
    name = "playlist",
    icon = Icons.Default.List,
    text = "Playlist",
    navController, scaffoldState
) {
    @Composable
    override fun Frame(context: AbstractActivity) {
        // todo scrollable
        Column {
            var playlists = remember { context.user?.playlists }
            CollapsableList(
                playlists?.map { it.toCollapsableListContent() } ?: emptyList()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                var isCreatingPlaylist by remember { mutableStateOf(false) }
                Icon(
                    if(isCreatingPlaylist) Icons.Default.Delete else Icons.Default.Add,
                    "New Playlist",
                    Modifier.clickable { isCreatingPlaylist = !isCreatingPlaylist },
                    Color.LightGray
                )
                if(isCreatingPlaylist) {
                    var newPlaylistName by remember { mutableStateOf("") }
                    var newPlaylistEnabled by remember { mutableStateOf(true) }

                    Checkbox(checked = newPlaylistEnabled, onCheckedChange = { newPlaylistEnabled = it })
                    TextField(
                        value = newPlaylistName,
                        onValueChange = { newPlaylistName = it },
                        label = { Text("Playlist Name") },
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                context.loadUser()?.let { user ->
                                    val p = user.id?.let{ userId ->
                                        Playlist.create(api, userId, Playlist(null, newPlaylistName, newPlaylistEnabled))
                                    }
                                    isCreatingPlaylist = false
                                    p?.let { context.user?.playlists?.add(it) }
                                    playlists = context.user?.playlists
                                    context.user?.save(context.userFile())
                                }
                            }
                        }
                    ) {
                        Text("Create")
                    }
                } else {
                    Text("Create New Playlist")
                }
            }
        }
    }
}