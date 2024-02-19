package net.tune_bot.view.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import net.tune_bot.model.Song

@Composable
fun SearchDialog(disable: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    val resultList = remember { mutableStateListOf<Song>() }

    if(isSearching) {
        SearchResultsDialog(
            songResults = resultList,
            disable = { isSearching = false },
            disableParent = disable
        )
    } else {
        AlertDialog(
            onDismissRequest = disable,
            title = { Text("Song Search") },
            text = {
                TextField(
                    searchQuery,
                    label = { Text("Search") },
                    onValueChange = { searchQuery = it }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        // TODO api search for song
                        resultList.add(Song(null, "Test Song", "Test Artist", "Test Album", 7357))
                        isSearching = true
                    }
                ) {
                    Text("Search")
                }
            },
            dismissButton = {
                Button(onClick = disable) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SearchResultsDialog(songResults: List<Song>, disable: () -> Unit, disableParent: () -> Unit) {
    var selectedIndex by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = disable,
        title = { Text(if(songResults.size == 1) "Is this your song?" else "Are any of these your song?" ) },
        text = {
            // radio buttons for each song
            Column {
                songResults.forEachIndexed { i, song ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = selectedIndex == i, onClick = { selectedIndex = i })
                        Text("${song.title} | ${song.artist} | ${song.album} | ${song.year}")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    println(songResults[selectedIndex].title)
                    disableParent()
                }
            ) {
                Text("Select Song")
            }
        },
        dismissButton = {
            Button(onClick = disable) {
                Text("No")
            }
        }
    )
}
