package net.tune_bot.view.component

import androidx.compose.foundation.clickable
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

interface CollapsableLazyList {
    @Composable fun Header()
    val list: List<@Composable () -> Unit>
}

@Composable
fun CollapsableLazyColumn(
    sections: List<CollapsableLazyList>,
    modifier: Modifier = Modifier
) {
    val collapsedState = remember(sections) { sections.map { true }.toMutableStateList() }

    LazyColumn(modifier) {
        sections.forEachIndexed { i, dataItem ->
            val collapsed = collapsedState[i]
            item(key = "header_$i") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        collapsedState[i] = !collapsed
                    }
                ) {
                    Icon(
                        if (collapsed) Icons.Default.KeyboardArrowDown
                        else Icons.Default.KeyboardArrowUp,
                        contentDescription = "",
                        tint = Color.LightGray,
                    )
                    dataItem.Header()
                }
                Divider()
            }
            if (!collapsed) {
                items(dataItem.list) { Row ->
                    Row {
                        Spacer(Modifier.size(24.dp))
                        Row()
                    }
                    Divider()
                }
            }
        }
    }
}

