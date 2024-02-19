package net.tune_bot.view.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

interface CollapsableListContent {
    fun onLongClick()
    @Composable fun Header()
    val contentList: List<@Composable () -> Unit>
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollapsableList(
    listContents: List<CollapsableListContent>,
    modifier: Modifier = Modifier
) {
    val collapsedState = remember(listContents) { listContents.map { true }.toMutableStateList() }

    LazyColumn(modifier) {
        listContents.forEachIndexed { i, listContent ->
            val collapsed = collapsedState[i]

            item(key = "header_$i") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.combinedClickable(
                        onClick = { collapsedState[i] = !collapsed },
                        onLongClick = { listContent.onLongClick() }
                    )
                ) {
                    Icon(
                        if (collapsed) Icons.Default.KeyboardArrowDown
                            else Icons.Default.KeyboardArrowUp,
                        if (collapsed) "Expand"
                            else "Collapse",
                        tint = Color.LightGray
                    )
                    listContent.Header()
                }
                Divider()
            }
            if (!collapsed) {
                items(listContent.contentList) { content ->
                    Row {
                        Spacer(Modifier.size(24.dp))
                        content()
                    }
                    Divider()
                }
            }
        }
    }
}

