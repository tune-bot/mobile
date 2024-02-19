package net.tune_bot.view.component

import android.view.KeyEvent
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction

@Composable
fun ActionTextField(value: String, onAction: (actionValue: String) -> Unit) {
    var rememberValue by remember { mutableStateOf(value) }
    TextField(
        value = rememberValue,
        onValueChange = { rememberValue = it },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onDone = { onAction(rememberValue) },
            onGo = { onAction(rememberValue) },
            onNext = { onAction(rememberValue) },
            onSend = { onAction(rememberValue) },
            onSearch = { onAction(rememberValue) }
        ),
        modifier = Modifier.onKeyEvent {
            if(it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                onAction(rememberValue)
            }
            false
        }
    )
}