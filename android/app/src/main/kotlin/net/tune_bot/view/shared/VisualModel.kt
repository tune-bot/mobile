package net.tune_bot.view.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import net.tune_bot.model.Playlist
import net.tune_bot.view.component.ActionTextField
import net.tune_bot.view.component.CollapsableListContent

@Composable
fun Playlist.toCollapsableListContent() = object: CollapsableListContent {
    var isEditing by remember { mutableStateOf(false) }

    override fun onLongClick() {
        isEditing = !isEditing
    }

    @Composable
    override fun Header() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            var isChecked by remember { mutableStateOf(enabled) }

            if(isEditing) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        enabled = isChecked
                        // todo send api request and save file
                    }
                )
            }

            if (isEditing && name != "Blacklist") {
                TextField(name, {name = it})
            } else {
                Text(name)
            }
        }
    }

    private val songContentList: List<@Composable () -> Unit> = songs.map {
        { Text("${it.title} | ${it.artist} | ${it.album} | ${it.year}") }
    }

    override val contentList: List<@Composable () -> Unit> = songContentList + {
        var isAddingSong by remember { mutableStateOf(false) }

        Icon(
            Icons.Default.Add,
            "Add Song",
            Modifier.clickable { isAddingSong = !isAddingSong }, // todo open song search modal
            Color.LightGray
        )
        Text("Add Song")

        if(isAddingSong) {
            SearchDialog { isAddingSong = false }
        }
    }
}