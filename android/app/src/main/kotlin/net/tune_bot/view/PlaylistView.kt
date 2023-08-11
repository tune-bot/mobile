package net.tune_bot.view

import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import net.tune_bot.controller.Api
import net.tune_bot.controller.MediaPlayer
import net.tune_bot.model.User

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
    @Composable
    override fun Frame() {
        Text("playlist")
    }
}