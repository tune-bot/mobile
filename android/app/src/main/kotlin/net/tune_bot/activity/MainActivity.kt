package net.tune_bot.activity

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import net.tune_bot.controller.MediaPlayer
import net.tune_bot.view.MainView

class MainActivity: AbstractActivity() {
    // TODO populate stuff from user
    // TODO setup media control (buttons, gestures, etc) to mediaPlayer
    @Composable
    override fun Content() = MainView(
        remember { MediaPlayer() },
        remember { api },
        rememberNavController(),
        rememberCoroutineScope(),
        rememberScaffoldState(),
        remember { getUser() }
    ).Frame()
}