package net.tune_bot.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.tune_bot.R
import net.tune_bot.activity.AbstractActivity
import net.tune_bot.controller.Api
import net.tune_bot.controller.MediaPlayer
import net.tune_bot.model.User

class MainView(
    private val mediaPlayer: MediaPlayer,
    private val api: Api,
    private val navController: NavHostController,
    private val coroutineScope: CoroutineScope,
    private val scaffoldState: ScaffoldState
): AbstractView(
    name = "main",
    icon = Icons.Default.Home,
    text = "Main",
    navController, scaffoldState
) {
    @Composable
    override fun Frame(context: AbstractActivity) {
        val views = listOf(
            QueueView(mediaPlayer, navController, coroutineScope, scaffoldState),
            PlaylistView(mediaPlayer, api, navController, coroutineScope, scaffoldState),
            BlacklistView(mediaPlayer, api, navController, coroutineScope, scaffoldState),
            SettingsView(navController, coroutineScope, scaffoldState)
        )

        Scaffold(
            scaffoldState = scaffoldState,
            drawerShape = object: Shape {
                override fun createOutline(
                    size: Size,
                    layoutDirection: LayoutDirection,
                    density: Density
                ) = Outline.Rectangle(
                    Rect(
                        Offset.Zero,
                        Offset(size.width * DRAWER_WIDTH_FRACTION, size.height)
                    )
                )
            },
            topBar = {
                TopAppBar(
                    title = {
                        Image( // TODO clean up the image
                            painter = painterResource(R.drawable.text_dark),
                            contentDescription = stringResource(R.string.app_name),
                            modifier = Modifier.height(24.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch { scaffoldState.drawerState.open() }
                            }
                        ) {
                            Icon(Icons.Default.Menu, "Open Navigation Menu")
                        }
                    }
                )
            },
            drawerContent =  {
                Column {
                    views.forEach { it.DrawerNav(coroutineScope) }
                }
            },
            content = { padding ->
                NavHost(
                    navController = navController,
                    startDestination = "playlist",
                    modifier = Modifier.padding(padding)
                ) {
                    views.forEach { view ->
                        composable(view.name) { view.Frame(context) }
                    }
                }
            },
            bottomBar = {
                BottomAppBar {
                    IconButton(onClick = mediaPlayer::previous) {
                        Icon(painterResource(android.R.drawable.ic_media_previous), "Previous Song")
                    }
                    IconButton(onClick = mediaPlayer::togglePlaying) {
                        Icon(
                            painterResource(
                                if(mediaPlayer.isPlaying) android.R.drawable.ic_media_pause
                                else android.R.drawable.ic_media_play
                            ),
                            if(mediaPlayer.isPlaying) "Pause" else "Play"
                        )
                    }
                    IconButton(onClick = mediaPlayer::next) {
                        Icon(painterResource(android.R.drawable.ic_media_next), "Next Song")
                    }

                    Text("0:00 / 3:33", fontSize = 8.sp) // TODO actual duration

                    Text(
                        mediaPlayer.current()?.let {
                            "${it.title} | ${it.artist} | ${it.album} | ${it.year}"
                        } ?: "No songs currently available.",
                        Modifier.padding(12.dp)
                    )

                    IconButton(
                        onClick = { mediaPlayer.shuffle() }
                    ) {
                        Icon(painterResource(android.R.drawable.ic_menu_share), "Shuffle") // TODO actual shuffle icon
                    }
                }
            }
        )
    }
}