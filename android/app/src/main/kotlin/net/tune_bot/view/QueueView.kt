package net.tune_bot.view

import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import net.tune_bot.controller.MediaPlayer
import net.tune_bot.model.User

class QueueView(
    mediaPlayer: MediaPlayer,
    navController: NavController,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    private val user: User?
): AbstractView(
    name = "queue",
    icon = Icons.Default.PlayArrow,
    text = "Queue",
    navController, scaffoldState
) {
    @Composable
    override fun Frame() {
        Text("queue")
    }
}