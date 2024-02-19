package net.tune_bot.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.tune_bot.activity.AbstractActivity

abstract class AbstractView(
    val name: String,
    private val icon: ImageVector,
    private val text: String,
    private val navController: NavController,
    private val scaffoldState: ScaffoldState
) {
    @Composable
    abstract fun Frame(context: AbstractActivity)

    @Composable
    fun DrawerNav(coroutineScope: CoroutineScope) = Card(
        modifier = Modifier
            .fillMaxWidth(DRAWER_WIDTH_FRACTION)
            .padding(4.dp)
            .clickable {
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                }
                navController.navigate(name)
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(icon, "$text Icon")

            Text(text, Modifier.padding(start = 24.dp))
        }
    }

    companion object {
        const val DRAWER_WIDTH_FRACTION = 0.34f
    }
}