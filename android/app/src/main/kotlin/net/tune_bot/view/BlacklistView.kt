package net.tune_bot.view

import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import net.tune_bot.activity.AbstractActivity
import net.tune_bot.controller.Api
import net.tune_bot.controller.MediaPlayer
import net.tune_bot.view.component.CollapsableList
import net.tune_bot.view.shared.toCollapsableListContent

class BlacklistView(
    mediaPlayer: MediaPlayer,
    api: Api,
    navController: NavController,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
): AbstractView(
    name = "blacklist",
    icon = Icons.Default.Close,
    text = "Blacklist",
    navController, scaffoldState
) {
    @Composable
    override fun Frame(context: AbstractActivity) {
        CollapsableList(
            context.user?.blacklist?.let { listOf(it.toCollapsableListContent()) } ?: emptyList()
        )
    }

}