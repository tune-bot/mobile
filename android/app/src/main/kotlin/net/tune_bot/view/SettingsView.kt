package net.tune_bot.view

import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import net.tune_bot.activity.AbstractActivity
import net.tune_bot.model.User

class SettingsView(
    navController: NavController,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
): AbstractView(
    name = "settings",
    icon = Icons.Default.Settings,
    text = "Settings",
    navController, scaffoldState
) {
    @Composable
    override fun Frame(context: AbstractActivity) {
        Text("settings")
        // download music
        // sync from server
        // logout
    }
}